package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.apache.coyote.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeQueueResponse;
import software.amazon.awssdk.services.connect.model.DescribeQueueRequest;
import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileResponse;
import software.amazon.awssdk.services.connect.model.DescribeUserResponse;
import software.amazon.awssdk.services.connect.model.DescribeUserRequest;
import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileRequest;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataRequest;
import software.amazon.awssdk.services.connect.model.GetCurrentUserDataResponse;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesResponse;
import software.amazon.awssdk.services.connect.model.SearchContactsResponse;
import software.amazon.awssdk.services.connect.model.SearchContactsTimeRange;
import software.amazon.awssdk.services.connect.model.SearchCriteria;
import software.amazon.awssdk.services.connect.model.SearchContactsRequest;
import software.amazon.awssdk.services.connect.model.SearchQueuesRequest;
import software.amazon.awssdk.services.connect.model.SearchQueuesResponse;
import software.amazon.awssdk.services.connect.model.UserDataFilters;
import tc3005b224.amazonconnectinsights.dto.queue.QueueCardDTO;
import tc3005b224.amazonconnectinsights.dto.queue.QueueDTO;
import tc3005b224.amazonconnectinsights.dto.queue.QueueMinDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionDTO;
import software.amazon.awssdk.services.connect.model.SearchRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.SearchRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.UserData;
import tc3005b224.amazonconnectinsights.dto.training.TrainingProgressItemDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Service
public class QueueService extends BaseService {
    @Autowired
    private MetricService metricService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private AgentService agentService;

    /**
     * Get all the queues general information required to display at the queue cards
     * if more specific information is required, use the findById method
     * Amount of API calls: 1 + 2n + c where n is the amount of agents
     * and c is the amount of total contacts all agents have
     * this is the worst case scenario where no contact has a negative sentiment
     * 
     * @param token
     * @param resourceId
     * @return Iterable<QueueCardDTO>
     * @throws BadRequestException
     * 
     * @author Moisés Adame
     * 
     * @see QueueCardDTO
     */
    public Iterable<QueueCardDTO> findAll(String token, String resourceId) throws BadRequestException {
        // Buld the request for getting the queues.
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Instantiate an iterarble of QueueCardDTO
        Set<QueueCardDTO> queueCardDTOs = new HashSet<QueueCardDTO>();

        Set<String> queueIds = new HashSet<String>();

        if(resourceId.equals("")){
            SearchQueuesResponse queues = getConnectClient(
                clientInfo.getAccessKeyId(), 
                clientInfo.getSecretAccessKey(), 
                clientInfo.getRegion()
            ).searchQueues(
                SearchQueuesRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .maxResults(100)
                .build()
            );

            // Iterate over the queue id's and get the queue information.
            queues.queues().forEach(
                queue -> {
                    // Request for describing an individual queue.
                    DescribeQueueRequest.Builder describeQueueRequest = DescribeQueueRequest.builder().instanceId(clientInfo.getInstanceId()).queueId(queue.queueId());
                    DescribeQueueResponse queuesInfo = getConnectClient(
                        clientInfo.getAccessKeyId(), 
                        clientInfo.getSecretAccessKey(), 
                        clientInfo.getRegion()
                    ).describeQueue(describeQueueRequest.build());

                    // Create a new QueueCardDTO object and add it to the iterable.
                    QueueCardDTO queueCardDTO = new QueueCardDTO(
                        queuesInfo.queue().queueId(),
                        queuesInfo.queue().queueArn(),
                        queuesInfo.queue().name(),
                        queuesInfo.queue().description(),
                        queuesInfo.queue().status().toString(),
                        queuesInfo.queue().maxContacts(),
                        findAgentsInQueue(token, queuesInfo.queue().queueId()).size()
                    );

                    queueCardDTOs.add(queueCardDTO);
                }
            );

        }else{
            // Get the queues associated to the agent
            ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), 
                clientInfo.getRegion()
            ).listRoutingProfileQueues(
                ListRoutingProfileQueuesRequest
                .builder()
                .instanceId(clientInfo.getInstanceId())
                .routingProfileId(resourceId).build()
            );

            // Iterate over the queue id's and get the queue information.
            routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(
                queue -> {
                    // Request for describing an individual queue.
                    DescribeQueueRequest.Builder describeQueueRequest = DescribeQueueRequest.builder().instanceId(clientInfo.getInstanceId()).queueId(queue.queueId());
                    DescribeQueueResponse queuesInfo = getConnectClient(
                        clientInfo.getAccessKeyId(), 
                        clientInfo.getSecretAccessKey(), 
                        clientInfo.getRegion()
                    ).describeQueue(describeQueueRequest.build());
                    
                    if (!queueIds.contains(queue.queueId())) {
                        // Create a new QueueCardDTO object and add it to the iterable.
                        QueueCardDTO queueCardDTO = new QueueCardDTO(
                            queuesInfo.queue().queueId(),
                            queuesInfo.queue().queueArn(),
                            queuesInfo.queue().name(),
                            queuesInfo.queue().description(),
                            queuesInfo.queue().status().toString(),
                            queuesInfo.queue().maxContacts(),
                            findAgentsInQueue(token, queuesInfo.queue().queueId()).size()
                        );
                        
                        queueCardDTOs.add(queueCardDTO);
                        queueIds.add(queue.queueId());
                    }
                }
            );
        }
        return queueCardDTOs;
    }

    /**
     * Get the number of agents in a queue using GetCurrentUserData
     * 
     * @param token
     * @param queueId
     * @return Integer
     * 
     * @author Moisés Adame
     * 
     */
    public List<UserData> findAgentsInQueue(String token, String queueId) {
        // Request for getting the users in the instance.
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        GetCurrentUserDataRequest.Builder searchUsersRequest = GetCurrentUserDataRequest.builder()
            .instanceId(clientInfo.getInstanceId())
            .filters(
                UserDataFilters.builder()
                    .queues(
                        new ArrayList<String>() {{
                            add(queueId);
                        }}
                    )
                    .build()
            );
        GetCurrentUserDataResponse userDataValues = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).getCurrentUserData(searchUsersRequest.build());

        return userDataValues.userDataList();
    }

    /**
     * Get the routing profiles of an specific queue.
     * 
     * @param token
     * @param queueId
     * @return String
     * 
     * @author Moisés Adame
     * @return 
     * @return 
     * 
     */
    public String getQueueRoutingProfiles(String token, String queueId) {
        // Create a client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Iterate over the routing profiles
        SearchRoutingProfilesRequest.Builder searchRoutingProfilesRequest = SearchRoutingProfilesRequest.builder().instanceId(clientInfo.getInstanceId());
        SearchRoutingProfilesResponse routingProfiles = getConnectClient(
            clientInfo.getAccessKeyId(), 
            clientInfo.getSecretAccessKey(), 
            clientInfo.getRegion()
        ).searchRoutingProfiles(searchRoutingProfilesRequest.build());

        // Set of routing profiles
        Set<String> routingProfileNames = new HashSet<String>();

        routingProfiles.routingProfiles().forEach(
            routingProfile -> {
                ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                    clientInfo.getAccessKeyId(),
                    clientInfo.getSecretAccessKey(),
                    clientInfo.getRegion()
                ).listRoutingProfileQueues(
                    ListRoutingProfileQueuesRequest
                    .builder()
                    .instanceId(clientInfo.getInstanceId())
                    .routingProfileId(routingProfile.routingProfileId())
                    .build()
                );

                routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(
                    routingProfileQueue -> {
                        if (routingProfileQueue.queueId().equals(queueId)) {
                            DescribeRoutingProfileResponse routingProfileInfo = getConnectClient(
                                clientInfo.getAccessKeyId(),
                                clientInfo.getSecretAccessKey(),
                                clientInfo.getRegion()
                            ).describeRoutingProfile(
                                DescribeRoutingProfileRequest
                                .builder()
                                .instanceId(clientInfo.getInstanceId())
                                .routingProfileId(routingProfile.routingProfileId())
                                .build()
                            );

                            routingProfileNames.add(routingProfileInfo.routingProfile().name());
                        }
                    }
                );
            }
        );

        // Join the set of routing profile names in a string
        String result = String.join(", ", routingProfileNames);

        return result;
    }
    
    /**
     * Get the information of a specific queue.
     * 
     * @param userUuid
     * @param queueId
     * @return InformationSectionListDTO
     * 
     * @see InformationSectionListDTO
     * @see InformationSectionDTO
     * @see ConnectClientInfo
     * @see DescribeQueueRequest
     * @see DescribeQueueResponse
     * @see BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public InformationSectionListDTO getQueueInformation(String userUuid, String queueId) {
        // Create the information section list.
        InformationSectionListDTO informationSectionListDTO = new InformationSectionListDTO();

        // Add title to the information section list.
        informationSectionListDTO.setSectionTitle("Information");

        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Request for describing an individual queue.
        DescribeQueueRequest.Builder describeQueueRequest = DescribeQueueRequest.builder().instanceId(clientInfo.getInstanceId()).queueId(queueId);
        DescribeQueueResponse queuesInfo = getConnectClient(
            clientInfo.getAccessKeyId(), 
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).describeQueue(describeQueueRequest.build());

        // Create a list of sections.
        List<InformationSectionDTO> sections = new ArrayList<>();
        sections.add(new InformationSectionDTO("Name", queuesInfo.queue().name(), "black"));
        sections.add(new InformationSectionDTO("Hours Of Operation", queuesInfo.queue().hoursOfOperationId(), "black"));
        sections.add(new InformationSectionDTO("Total Agents", Integer.toString(findAgentsInQueue(userUuid, queueId).size()), "black"));
        sections.add(new InformationSectionDTO("Skills", getQueueRoutingProfiles(userUuid, queueId), "black"));
        sections.add(getQueueContactsInformationSection(userUuid, queueId, queuesInfo.queue().maxContacts()));

        // Add the sections to the information section list.
        informationSectionListDTO.setSections(sections);
        
        return informationSectionListDTO;
    }

    /**
     * Get the information of a specific queue
     * 
     * @param userUuid
     * @param queueId
     * @return QueueDTO
     * @throws BadRequestException
     * 
     * @see QueueDTO
     * @see BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public QueueDTO findById(String userUuid, String queueId) throws BadRequestException {
        // Request for getting the queue information.
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        DescribeQueueRequest.Builder describeQueueRequest = DescribeQueueRequest.builder().instanceId(clientInfo.getInstanceId()).queueId(queueId);
        DescribeQueueResponse queuesInfo = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).describeQueue(describeQueueRequest.build());

        List<UserData> agents = findAgentsInQueue(userUuid, queueId);

        // Create a new QueueDTO object and return it.
        return new QueueDTO(
            queuesInfo.queue().queueId(),
            queuesInfo.queue().queueArn(),
            getQueueInformation(userUuid, queueId),
            metricService.getMetricsById(userUuid, "QUEUE", queuesInfo.queue().queueArn()),
            alertService.findAll(userUuid , "", queuesInfo.queue().queueArn(), "false"),
            getTrainings(userUuid, queuesInfo.queue().queueArn(), agents),
            agentService.findAll(userUuid, queueId)
        );
    }

    /**
     * Get the number of contacts of an especific queue.
     * 
     * @param userUuid
     * @param queueId
     * @return InformationSectionDTO
     * 
     * @author Moisés Adame
     * 
     */
    public InformationSectionDTO getQueueContactsInformationSection(String userUuid, String queueId, Integer maxContacts) {
        if(maxContacts == null){
            return new InformationSectionDTO("Contacts", "Unlimited", "green");
        }
        
        // Request for getting the queue information.
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        List<String> queueIds = new ArrayList<>();
        queueIds.add(queueId);

        SearchContactsResponse contacts = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).searchContacts(
            SearchContactsRequest
            .builder()
            .instanceId(clientInfo.getInstanceId())
            .searchCriteria(
                SearchCriteria
                .builder()
                .queueIds(queueIds)
                .build()
            )
            .timeRange(
                SearchContactsTimeRange
                .builder()
                .startTime(Instant.now().minusSeconds(7200))
                .endTime(Instant.now())
                .type("CONNECTED_TO_AGENT_TIMESTAMP")
                .build()
            )
            .build()
        );

        // Create a new InformationSectionDTO object and return it.
        String bannerColor = "green";
        if(contacts.contacts().size() == maxContacts){
            bannerColor = "red";
        }else if(contacts.contacts().size() >= maxContacts * 0.5){
            bannerColor = "yellow";
        }
        
        return new InformationSectionDTO("Contacts", String.valueOf(contacts.contacts().size()), bannerColor);
    }

    /**
     * Get trainings of a specific queue.
     * 
     * @param userUuid
     * @param queueId
     * @return List<TrainingProgressItemDTO>
     * 
     * @author Moisés Adame
     * 
     */
    public List<TrainingProgressItemDTO> getTrainings(String userUuid, String queueArn, List<UserData> agents) {
        // Create a new list of TrainingProgressItemDTO
        List<TrainingProgressItemDTO> trainings = new ArrayList<TrainingProgressItemDTO>();

        // Map of total trainings per agent.
        Map<String, Integer> totalTrainings = new HashMap<String, Integer>();
        Map<String, Float> trainingProgress = new HashMap<String, Float>();

        // Insntiate a client info.
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Iterate over the agents and get the total trainings per agent.
        agents.forEach(
            agent -> {
                Iterable<Alert> alerts = alertService.findTrainingAlerts(userUuid, agent.user().arn());
                alerts.forEach(
                    alert -> {
                        Integer totalTraings = totalTrainings.get(agent.user().arn());
                        if(totalTraings == null){
                            totalTrainings.put(agent.user().arn(), 1);
                        }else{
                            totalTrainings.put(agent.user().arn(), totalTraings + 1);
                        }
                    }
                );
            }
        );

        // Iterate over the agents and get their progress.
        agents.forEach(
            agent -> {
                Iterable<Alert> alerts = alertService.findTrainingAlerts(userUuid, agent.user().arn());
                alerts.forEach(
                    alert -> {
                        if(alert.getTrainingCompleted()){
                            Float progress = trainingProgress.get(agent.user().arn());
                            if(progress == null){
                                trainingProgress.put(agent.user().arn(), 1f / totalTrainings.get(agent.user().arn()).floatValue());
                            }else{
                                trainingProgress.put(agent.user().arn(), progress + 1f / totalTrainings.get(agent.user().arn()).floatValue());
                            }
                        }else{
                            Float progress = trainingProgress.get(agent.user().arn());
                            if(progress == null){
                                trainingProgress.put(agent.user().arn(), 0f);
                            }
                        }
                    }
                );
            }
        );

        // Iterate over the progress map and fill up trainings list.
        trainingProgress.forEach(
            (arn, progress) -> {
                DescribeUserResponse user = getConnectClient(
                    getConnectClientInfo(userUuid).getAccessKeyId(),
                    getConnectClientInfo(userUuid).getSecretAccessKey(),
                    getConnectClientInfo(userUuid).getRegion()
                ).describeUser(
                    DescribeUserRequest
                    .builder()
                    .instanceId(clientInfo.getInstanceId())
                    .userId(arn)
                    .build()
                );
                trainings.add(new TrainingProgressItemDTO(user.user().username(), trainingProgress.get(arn)));
            }
        );

        return trainings;
    }

    /**
     * Get a list of the AgentDTO (complete information) of the agents
     * in the queue.
     * 
     * @param token
     * @param agents
     * @return List<AgentDTO>
     * 
     * @author Moisés Adame
     * 
     */
    public List<AgentDTO> getAgentInformationInQueue(String token, List<UserData> agents) {
        // List of the agentDTO's
        List<AgentDTO> agentDTOs = new ArrayList<AgentDTO>();

        // Iterate over the agents and get their information.
        agents.forEach(
            agent -> {
                try {
                    agentDTOs.add(agentService.findById(token, agent.user().arn()));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        );

        return agentDTOs;
    }

    public Iterable<QueueMinDTO> findAllMin (String token, String resourceId) throws BadRequestException {
        // Buld the request for getting the queues.
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Instantiate an iterarble of QueueCardDTO
        Set<QueueMinDTO> queueMinDTOs = new HashSet<QueueMinDTO>();

        if(resourceId.equals("")){
            SearchQueuesResponse queues = getConnectClient(
                clientInfo.getAccessKeyId(), 
                clientInfo.getSecretAccessKey(), 
                clientInfo.getRegion()
            ).searchQueues(
                SearchQueuesRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .maxResults(100)
                .build()
            );
            queues.queues().forEach(
                queue -> {
                    // Create a new QueueCardDTO object and add it to the iterable.
                    QueueMinDTO queueMinDTO = new QueueMinDTO(
                        queue.queueId(),
                        queue.name()
                    );

                    queueMinDTOs.add(queueMinDTO);
                }
            );
        }else {
            ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), 
                clientInfo.getRegion()
            ).listRoutingProfileQueues(
                ListRoutingProfileQueuesRequest
                .builder()
                .instanceId(clientInfo.getInstanceId())
                .routingProfileId(resourceId).build()
            );

            Set<String> queueIds = new HashSet<String>();
            routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(
                queue -> {
                    if (queueIds.add(queue.queueId())){
                        // Create a new QueueCardDTO object and add it to the iterable.
                        QueueMinDTO queueMinDTO = new QueueMinDTO(
                            queue.queueId(),
                            queue.queueName()
                        );
                        
                        queueMinDTOs.add(queueMinDTO);
                    }
                }
            );
        }
        return queueMinDTOs;
    }
}

