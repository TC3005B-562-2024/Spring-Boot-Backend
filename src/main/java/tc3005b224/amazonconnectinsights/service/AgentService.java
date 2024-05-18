package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.GetCurrentUserDataRequest;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataResponse;
import software.amazon.awssdk.services.connect.model.ListRealtimeContactAnalysisSegmentsV2Request;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesResponse;
import software.amazon.awssdk.services.connect.model.RealTimeContactAnalysisSegmentType;
import software.amazon.awssdk.services.connect.model.SearchUsersRequest;
import software.amazon.awssdk.services.connect.model.SearchUsersRequest.Builder;
import software.amazon.awssdk.services.connect.model.SearchUsersResponse;
import software.amazon.awssdk.services.connect.model.StringCondition;
import software.amazon.awssdk.services.connect.model.UserDataFilters;
import software.amazon.awssdk.services.connect.model.UserSearchCriteria;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.AgentInformationDTO;
import tc3005b224.amazonconnectinsights.dto.information.ContactInformationDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingDTO;

@Service
public class AgentService extends BaseService {
    @Autowired
    private SkillService skillService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private TrainingsService trainingsService;

    /**
     * Get all the agents general information required to display at the agent cards
     * if more specific information is required, use the findById method
     * Amount of API calls: 1 + 2n + c where n is the amount of agents
     * and c is the amount of total contacts all agents have
     * this is the worst case scenario where no contact has a negative sentiment
     * 
     * @param token
     * @param queues
     * @param skills
     * @return Iterable<AgentCardDTO>
     * @throws BadRequestException
     * 
     * @author Diego Jacobo Djmr5
     * 
     * @see AgentCardDTO
     */
    public Iterable<AgentCardDTO> findAll(String token, String resourceId)
            throws BadRequestException {
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        // Get all the agents
        Builder searchUserRequest = SearchUsersRequest.builder().instanceId(clientInfo.getInstanceId());

        // If a resourceId is provided, filter the agents by that resourceId
        if (!resourceId.isEmpty()) {
            UserSearchCriteria criteria = UserSearchCriteria.builder().stringCondition(
                    StringCondition.builder().comparisonType("EXACT").fieldName("ResourceId").value(resourceId).build())
                    .build();
            searchUserRequest.searchCriteria(criteria);
        }

        // Get the agents / users from the instance
        SearchUsersResponse users = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .searchUsers(searchUserRequest.build());

        System.out.println(users);
        // Create the filters to get the user data from the agents retrieved
        Collection<String> userIds = new ArrayList<String>();
        users.users().forEach(
                user -> {
                    userIds.add(user.id());
                });
        
        
        // Get the contacts information for the agents
        GetCurrentUserDataResponse getCurrentUserDataResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .getCurrentUserData(GetCurrentUserDataRequest.builder()
                        .instanceId(clientInfo.getInstanceId())
                        .filters(UserDataFilters.builder().agents(userIds).build())
                        .build());
        System.out.println(getCurrentUserDataResponse);

        if (getCurrentUserDataResponse.userDataList().isEmpty()) {
            // TODO: If there are no agents making or receiving calls, then do something 
            // Check if the there is agents connected to the instance but not making calls
            // and if these agents will be returned by the getCurrentUserDataResponse
            //--------------------------------------------------------------------
            //--------------------------------------------------------------------
            //--------------------------------------------------------------------
            //-----------Revisa esta cosa Diego del futuro jajaja-----------------
            //--------------------------------------------------------------------
            //--------------------------------------------------------------------
            //--------------------------------------------------------------------

            System.out.println("Agents are not currently making or receiving calls");
        }

        // Estructura de datos bien loca que tiene un Map juntando los ids de los
        // usuarios de ambas responses
        // y los contactos de cada usuario si es que tiene, si no tiene contactos, es
        // una lista vacia
        Map<String, List<String>> contacts = new HashMap<String, List<String>>();
        users.users().forEach(
                user -> {
                    List<String> contactIds = new ArrayList<String>();
                    getCurrentUserDataResponse.userDataList().forEach(
                            userData -> {
                                if (userData.user().id().equals(user.id())) {
                                    userData.contacts().forEach(
                                            contact -> {
                                                contactIds.add(contact.contactId());
                                            });
                                }
                            });
                    contacts.put(user.id(), contactIds);
                });




        List<AgentCardDTO> agents = new ArrayList<AgentCardDTO>();

        users.users().forEach(
                userData -> {
                    // Get the details of the routing profile
                    ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                            clientInfo.getAccessKeyId(),
                            clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                            .listRoutingProfileQueues(
                                    ListRoutingProfileQueuesRequest.builder().instanceId(clientInfo.getInstanceId())
                                            .routingProfileId(userData.routingProfileId()).build());
                    List<String> skillsList = new ArrayList<String>();
                    routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(queue -> {
                        skillsList.add(queue.queueName());
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
                                            .segments().get(0).transcript().sentiment().name());
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

                    // TODO: Get the status of the agent
                    // Supongo que aqui es sacar el estatus porque no viene en otro lado kaajajajajaja
                    // Checar: https://docs.aws.amazon.com/connect/latest/APIReference/API_ListAgentStatuses.html
                    //--------------------------------------------------------------------
                    //--------------------------------------------------------------------
                    //--------------------------------------------------------------------
                    //-----------Revisa esta cosa Diego del futuro jajaja-----------------
                    //--------------------------------------------------------------------
                    //--------------------------------------------------------------------
                    //--------------------------------------------------------------------

                    agents.add(new AgentCardDTO(
                            userData.id(),
                            userData.identityInfo().firstName() + " "
                                    + userData.identityInfo().lastName(),
                            "DISCONNECTED",
                            worstSentiment,
                            skillsList,
                            alertService.findHighestPriority(token, userData.arn()).getHighestPriorityAlert()));
                });

        return agents;
    };

    public AgentDTO findById(String instanceId, String agentId) throws Exception {
        // TODO: Call Amazon Connect's DescribeUser endpoint and populate AgentDTO.
        // TODO: Call Amazon Connect's Contact Lens and check if it exists, if it does use it
        // TODO: Find Out how to connect agent, user and contact.

        String resource = "agent:" + agentId;

        ContactInformationDTO contactInformationDTO = new ContactInformationDTO("123", "1:43 min", true, "POSITIVE");
        AlertPriorityDTO alertPriorityDTO = alertService.findByResource(1, resource);

        try {
            List<SkillBriefDTO> skills = skillService.findByAgentId(instanceId, agentId);
            AgentInformationDTO agentInformationDTO = new AgentInformationDTO("John Doe", skills.get(0).getAlias(), "ROUTABLE");
            Iterable<TrainingDTO> trainings = trainingsService.findAll(resource, "", "true");

            return new AgentDTO(agentId, resource, skills, agentInformationDTO, contactInformationDTO, alertPriorityDTO, trainings);
        }catch (Error e) {
            throw new Exception("Something failed.");
        }
    }
}
