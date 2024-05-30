package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeContactRequest;
import software.amazon.awssdk.services.connect.model.DescribeContactResponse;
import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileRequest;
import software.amazon.awssdk.services.connect.model.DescribeUserRequest;
import software.amazon.awssdk.services.connect.model.DescribeUserResponse;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataRequest;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataResponse;
import software.amazon.awssdk.services.connect.model.ListRealtimeContactAnalysisSegmentsV2Request;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesResponse;
import software.amazon.awssdk.services.connect.model.RealTimeContactAnalysisSegmentType;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;
import software.amazon.awssdk.services.connect.model.StringCondition;
import software.amazon.awssdk.services.connect.model.UserData;
import software.amazon.awssdk.services.connect.model.UserDataFilters;
import software.amazon.awssdk.services.connect.model.UserSearchCriteria;
import software.amazon.awssdk.services.connect.model.UserSearchSummary;
import tc3005b224.amazonconnectinsights.dto.agent.AgentAvailableToTransferListDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentMinimalDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.AgentInformationDTO;
import tc3005b224.amazonconnectinsights.dto.information.ContactInformationDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.utils.IdAndNameDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Service
public class AgentService extends BaseService {
    @Autowired
    private AlertService alertService;

    @Autowired
    private TrainingsService trainingsService;

    @Autowired
    private MetricService metricService;

    @Autowired
    private RoutingProfileService routingProfileService;

    @Autowired
    private UserService userService;

    /**
     * Get all the agents general information required to display at the agent cards
     * if more specific information is required, use the findById method
     * Amount of API calls: 2 + n + c where n is the amount of agents
     * and c is the amount of total contacts all agents have
     * this is the worst case scenario where no contact has a negative sentiment
     * 
     * @param userUuid
     * @param resourceId
     * @return Iterable<AgentCardDTO>
     * @throws BadRequestException
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see AgentCardDTO
     */
    public Iterable<AgentCardDTO> findAll(String userUuid, String resourceId)
            throws BadRequestException {
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Get all the agents
        List<UserSearchSummary> users = userService.searchUsers(userUuid);

        // If a resourceId is provided, filter the agents by that resourceId
        if (!resourceId.isEmpty()) {
            users = userService.searchUsers(userUuid, resourceId);
        } else {
            users = userService.searchUsers(userUuid);
        }

        if (users.isEmpty()) {
            // If there is no agents with the resourceId, try to get the agents from the
            // queues
            // The resourceId might be from a queue, in that case, get the agents from the
            // routing profile
            try {
                Iterable<RoutingProfileSummary> routingProfiles = routingProfileService.getRoutingProfiles(userUuid);
                Set<String> routingProfileIdsAssociatedToTheQueue = new HashSet<>();
                routingProfiles.forEach(routingProfile -> {
                    ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                            clientInfo.getAccessKeyId(),
                            clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                            .listRoutingProfileQueues(
                                    ListRoutingProfileQueuesRequest.builder().instanceId(clientInfo.getInstanceId())
                                            .routingProfileId(routingProfile.id()).build());
                    for (int j = 0; j < routingProfileQueues.routingProfileQueueConfigSummaryList().size(); j++) {
                        if (routingProfileQueues.routingProfileQueueConfigSummaryList().get(j).queueId()
                                .equals(resourceId)) {
                            routingProfileIdsAssociatedToTheQueue.add(routingProfile.id());
                        }
                    }
                });
                if (routingProfileIdsAssociatedToTheQueue.size() > 0) {
                    // Reset the searchUserRequest
                    List<UserSearchCriteria> criterias = new ArrayList<UserSearchCriteria>();

                    routingProfileIdsAssociatedToTheQueue.forEach(
                            routingProfileSetValue -> {
                                criterias.add(UserSearchCriteria.builder()
                                        .stringCondition(StringCondition.builder().comparisonType("EXACT")
                                                .fieldName("RoutingProfileId")
                                                .value(routingProfileSetValue).build())
                                        .build());
                            });
                    // Retrieve the new agents associated to the routing profiles associated to the
                    // queue
                    users = userService.searchUsers(userUuid, criterias);
                } else {
                    throw new BadRequestException("Sorry, we could found any agent in the resourceId, Xd");
                }
            } catch (Exception e2) {
                throw new BadRequestException(
                        "Sorry, there was an error retrieving the agents. Please review the parameters provided.");
            }
        }

        // Create the filters to get the user data from the agents retrieved
        Collection<String> userIds = new ArrayList<String>();
        users.forEach(user -> {
            userIds.add(user.id());
        });
        if (userIds.isEmpty())
            throw new BadRequestException("There are no agents with the resourceId provided");

        // Get the contacts information for the agents
        List<UserData> getCurrentUserDataResponse = userService.getCurrentUserData(userUuid, userIds);

        // Get the contacts and status for the agents
        // If there are no values in the getCurrentUserDataResponse, create an empty map
        Map<String, List<String>> contacts = new HashMap<String, List<String>>();
        Map<String, String> agentStatus = new HashMap<String, String>();
        if (!getCurrentUserDataResponse.isEmpty()) {
            // Create a map with the contacts for each agent
            users.forEach(user -> {
                List<String> contactIds = new ArrayList<String>();
                getCurrentUserDataResponse.forEach(userData -> {
                    if (userData.user().id().equals(user.id())) {
                        // Add all the contacts to the list
                        userData.contacts().forEach(contact -> {
                            contactIds.add(contact.contactId());
                        });
                        // Add the status of the agent
                        agentStatus.put(user.id(), userData.status().statusName());
                    }
                });
                contacts.put(user.id(), contactIds);
            });
        }

        // Create the list of agents to return
        List<AgentCardDTO> agents = new ArrayList<AgentCardDTO>();
        users.forEach(
                userData -> {
                    // Get the details of the routing profile
                    ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                            clientInfo.getAccessKeyId(),
                            clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                            .listRoutingProfileQueues(
                                    ListRoutingProfileQueuesRequest.builder().instanceId(clientInfo.getInstanceId())
                                            .routingProfileId(userData.routingProfileId()).build());
                    Set<String> queuesSet = new HashSet<>();
                    routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(queue -> {
                        queuesSet.add(queue.queueName());
                    });

                    // Get the MOST NEGATIVE sentiment status of contacts for the agent
                    // IF Contact Lens is enabled
                    String worstSentiment = null;
                    if (clientInfo.getContactLensEnabled() && contacts.get(userData.id()).size() > 0) {
                        List<String> sentiments = new ArrayList<String>();
                        for (String contactId : contacts.get(userData.id())) {

                            // Get the segments for the contact
                            sentiments.add(
                                    getConnectClient(clientInfo.getAccessKeyId(), clientInfo.getSecretAccessKey(),
                                            clientInfo.getRegion())
                                            .listRealtimeContactAnalysisSegmentsV2(
                                                    ListRealtimeContactAnalysisSegmentsV2Request.builder()
                                                            .instanceId(clientInfo.getInstanceId())
                                                            .contactId(contactId)
                                                            .segmentTypes(
                                                                    RealTimeContactAnalysisSegmentType.TRANSCRIPT)
                                                            .build())
                                            .segments().get(0).transcript().sentiment().toString());
                            if (sentiments.contains("NEGATIVE"))
                                break;
                        }
                        if (sentiments.size() > 0) {
                            if (sentiments.contains("NEGATIVE")) {
                                worstSentiment = "NEGATIVE";
                            } else if (sentiments.contains("NEUTRAL")) {
                                worstSentiment = "NEUTRAL";
                            } else if (sentiments.contains("POSITIVE")) {
                                worstSentiment = "POSITIVE";
                            }
                        }
                    }

                    agents.add(new AgentCardDTO(
                            userData.id(),
                            userData.arn(),
                            userData.identityInfo().firstName() + " "
                                    + userData.identityInfo().lastName(),
                            agentStatus.getOrDefault(userData.id(), "DISCONNECTED"),
                            worstSentiment,
                            queuesSet,
                            alertService.findHighestPriority(userUuid, userData.arn()).getHighestPriorityAlert()));
                });
        return agents;
    };

    /**
     * Get the detailed agent information by the agentId
     * 
     * @param userUuid
     * @param agentId
     * @return AgentDTO
     * @throws Exception
     * 
     * @author Moisés Adame MoisesAdame
     * @author Diego Jacobo Djmr5
     * 
     * @see AgentDTO
     */
    public AgentDTO findById(String userUuid, String agentId) throws Exception {
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Get the agent general information
        DescribeUserResponse agent = getConnectClient(clientInfo.getAccessKeyId(), clientInfo.getSecretAccessKey(),
                clientInfo.getRegion()).describeUser(
                        DescribeUserRequest.builder().instanceId(clientInfo.getInstanceId()).userId(agentId).build());

        // Get the alerts of the agent
        AlertPriorityDTO alerts = alertService.findByResource(userUuid, agent.user().arn());

        // Get the real-time data of the agent
        List<UserData> agentCurrentDataResponse = userService.getCurrentUserData(userUuid, List.of(agentId));

        // Routing profile name
        String routingProfileName = getConnectClient(clientInfo.getAccessKeyId(), clientInfo.getSecretAccessKey(),
                clientInfo.getRegion()).describeRoutingProfile(
                        DescribeRoutingProfileRequest.builder().instanceId(clientInfo.getInstanceId())
                                .routingProfileId(agent.user().routingProfileId()).build())
                .routingProfile().name();

        // Obtener contactos
        // TODO: Obtener el promedio de las duraciones de las llamadas
        List<ContactInformationDTO> contacts = new ArrayList<>();
        agentCurrentDataResponse.forEach(userData -> {
            userData.contacts().forEach(contact -> {
                // TODO: Review, it can be different due to the timezones
                DescribeContactResponse contactDetails = getConnectClient(clientInfo.getAccessKeyId(),
                        clientInfo.getSecretAccessKey(),
                        clientInfo.getRegion()).describeContact(
                                DescribeContactRequest.builder()
                                        .instanceId(clientInfo.getInstanceId())
                                        .contactId(contact.contactId())
                                        .build());
                String sentiment = null;
                if (clientInfo.getContactLensEnabled()) {
                    sentiment = getConnectClient(clientInfo.getAccessKeyId(), clientInfo.getSecretAccessKey(),
                            clientInfo.getRegion()).listRealtimeContactAnalysisSegmentsV2(
                                    ListRealtimeContactAnalysisSegmentsV2Request.builder()
                                            .instanceId(clientInfo.getInstanceId())
                                            .contactId(contact.contactId())
                                            .segmentTypes(RealTimeContactAnalysisSegmentType.TRANSCRIPT)
                                            .build())
                            .segments().get(0).transcript().sentiment().toString();
                }
                // TODO: Change the durationAboveAverage to the real value
                contacts.add(new ContactInformationDTO(contact.contactId(),
                        contactDetails.contact().agentInfo() != null
                                ? contactDetails.contact().agentInfo().connectedToAgentTimestamp().toString()
                                : null,
                        true, sentiment));
            });
        });

        // Sacar queues
        ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .listRoutingProfileQueues(
                        ListRoutingProfileQueuesRequest.builder().instanceId(clientInfo.getInstanceId())
                                .routingProfileId(agent.user().routingProfileId()).build());
        Set<String> queuesSet = new HashSet<>();
        List<IdAndNameDTO> queues = new ArrayList<>();
        routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(queue -> {
            if (queuesSet.add(queue.queueName())) {
                queues.add(new IdAndNameDTO(queue.queueId(), queue.queueName()));
            }
        });

        String name = agent.user().identityInfo().firstName() + " " + agent.user().identityInfo().lastName();
        String agentStatus = null;
        if (!agentCurrentDataResponse.isEmpty()) {
            agentStatus = agentCurrentDataResponse.get(0).status().statusName();
        }
        AgentInformationDTO agentInformation = new AgentInformationDTO(name, routingProfileName,
                agentStatus);
        Iterable<Alert> trainings = trainingsService
                .findTrainingsAlertsByResource(clientInfo.getIdentifier(), agent.user().arn());

        // Get the agent metrics
        InformationSectionListDTO metrics = metricService.getMetricsById(userUuid, "AGENT", agent.user().arn());

        return new AgentDTO(
                agentId,
                agent.user().arn(),
                queues,
                agentInformation,
                contacts,
                alerts,
                trainings,
                metrics);
    }

    /**
     * Get the available agents that are not in a given routing profile. Access to
     * the agents attribute of the returned object to get the list of agents. The
     * cappedRoutingProfileId attribute is the routing profile id that was used to
     * get the agents that are not within it.
     * 
     * @param userUuid
     * @param routingProfileId
     * @return AgentAvailableToTransferListDTO
     * @throws Exception
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see AgentAvailableToTransferListDTO
     */
    public AgentAvailableToTransferListDTO findAvailableAgentNotInRoutingProfile(String userUuid,
            String routingProfileId)
            throws Exception {
        // Handle exceptions
        if (routingProfileId == null || routingProfileId.isEmpty())
            throw new BadRequestException("The routingProfileId is required");
        if (userUuid == null || userUuid.isEmpty())
            throw new BadRequestException("The user information was not provided");

        // Get the client info based on the token
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Get all the agents from the routing profile
        Iterable<UserSearchSummary> routingProfileUsers = userService.searchUsers(userUuid, routingProfileId);

        List<String> agentsIdsInRoutingProfile = new ArrayList<>();
        routingProfileUsers.forEach(user -> {
            agentsIdsInRoutingProfile.add(user.id());
        });

        // Get the real-time data of the agent
        Iterable<RoutingProfileSummary> skills = routingProfileService.getRoutingProfiles(userUuid);
        List<String> skillsIds = new ArrayList<>();
        skills.forEach(skill -> {
            if (!skill.id().equals(routingProfileId)) {
                skillsIds.add(skill.id());
            }
        });

        GetCurrentUserDataResponse agentCurrentDataResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .getCurrentUserData(GetCurrentUserDataRequest.builder()
                        .instanceId(clientInfo.getInstanceId())
                        .filters(UserDataFilters.builder().routingProfiles(skillsIds).build())
                        .build());

        // Create the list of available agents not in the routing profile
        List<AgentMinimalDTO> availableAgents = new ArrayList<>();
        agentCurrentDataResponse.userDataList().forEach(userData -> {
            if (userData.status().statusName().equals("Available")
                    && !agentsIdsInRoutingProfile.contains(userData.user().id()) && userData.contacts().isEmpty()) {
                availableAgents.add(new AgentMinimalDTO(
                        userData.user().id(),
                        null,
                        userData.status().statusName(),
                        userData.routingProfile().id()));
            }
        });

        // Obtener contactos
        return new AgentAvailableToTransferListDTO(routingProfileId, availableAgents);
    }
}
