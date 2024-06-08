package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.ListUsersRequest;
import software.amazon.awssdk.services.connect.model.ListUsersResponse;
import software.amazon.awssdk.services.connect.model.SearchQueuesRequest;
import software.amazon.awssdk.services.connect.model.SearchQueuesResponse;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Service
public class AlertEngineService extends BaseService {
    @Autowired
    private MetricService metricService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private AlertService alertService;

    @Autowired
    private AgentService agentService;

    /**
     * Service that retrieves all the routing profiles arn's of the instance
     * @param token
     * 
     * @return List<String>
     * 
     * @throws BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public List<String> getRoutingProfilesArns(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Get the routing profiles
        ListRoutingProfilesResponse routingProfiles = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).listRoutingProfiles(
            ListRoutingProfilesRequest
                .builder()
                .instanceId(clientInfo.getInstanceId())
                .build()
        );

        // Instantiate the list of routing profiles
        List<String> routingProfilesArn = new ArrayList<>();

        // Add the arn's to the list
        routingProfiles.routingProfileSummaryList().forEach(routingProfile -> {
            routingProfilesArn.add(routingProfile.arn());
        });

        return routingProfilesArn;
    }

    /**
     * Service that retrieves all the queues arn's of the instance
     * @param token
     * 
     * @return List<String>
     * 
     * @throws BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public List<String> getQueuesArns(String token, Iterable<String> routingProfiles) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Get the queues
        SearchQueuesResponse queues = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).searchQueues(
            SearchQueuesRequest
                .builder()
                .instanceId(clientInfo.getInstanceId())
                .maxResults(100)
                .build()
        );

        // Instantiate the list of queues
        List<String> queuesArn = new ArrayList<>();

        // Add the arn's to the list
        queues.queues().forEach(queue -> {
            queuesArn.add(queue.queueArn());
        });

        return queuesArn;
    }

    /**
     * Service that retrieves all the agents arn's of the instance
     * @param token
     * 
     * @return List<String>
     * 
     * @throws BadRequestException
     * 
     * @author Moisés Adame
     * 
     */
    public List<String> getAgentsArns(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Get the agents
        ListUsersResponse agents = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).listUsers(
            ListUsersRequest
                .builder()
                .instanceId(clientInfo.getInstanceId())
                .build()
        );

        // Instantiate the list of agents
        List<String> agentsArn = new ArrayList<>();

        // Add the arn's to the list
        agents.userSummaryList().forEach(agent -> {
            agentsArn.add(agent.arn());
        });

        return agentsArn;
    }

    /**
     * Service that analyzes the metrics of the instance and generates alerts if necessary.
     * @param token
     * 
     * @return void
     * 
     * @see getAgentsArns
     * @see getQueuesArns
     * @see getRoutingProfilesArns
     * @see getMetricsById
     * 
     * @author Moisés Adame
     * 
     */
    public void generateAlerts(String token) {
        try {
            List<String> routingProfiles = getRoutingProfilesArns(token);
            List<String> queues = getQueuesArns(token, routingProfiles);
            List<String> agents = getAgentsArns(token);

            ConnectClientInfo clientInfo = getConnectClientInfo(token);

            routingProfiles.forEach(
                routingProfileArn -> {
                    try {
                        InformationMetricSectionListDTO metrics = metricService.getMetricsById(token, "ROUTING_PROFILE", routingProfileArn);
                        System.out.println("Routing Profile: " + routingProfileArn);
                        System.out.println(metrics.toString());
                        analyzeMetrics(clientInfo.getIdentifier().shortValue(), metrics, "ROUTING_PROFILE", routingProfileArn);
                    } catch (BadRequestException e) {
                        System.out.println("Catch: " + e.getMessage());
                    }
                }
            );

            queues.forEach(
                queueArn -> {
                    try {
                        InformationMetricSectionListDTO metrics = metricService.getMetricsById(token, "QUEUE", queueArn);
                        System.out.println("Queue: " + queueArn);
                        System.out.println(metrics.toString());
                        analyzeMetrics( clientInfo.getIdentifier().shortValue(), metrics, "QUEUE", queueArn);
                    } catch (BadRequestException e) {
                        System.out.println("Catch: " + e.getMessage());
                    }
                }
            );

            agents.forEach(
                agentArn -> {
                    try {
                        InformationMetricSectionListDTO metrics = metricService.getMetricsById(token, "AGENT", agentArn);
                        System.out.println("Agent: " + agentArn);
                        analyzeMetrics( clientInfo.getIdentifier().shortValue(), metrics, "AGENT", agentArn);
                    } catch (BadRequestException e) {
                        System.out.println("Catch: " + e.getMessage());
                    }
                }
            );
        } catch (Exception e) {
            System.out.println("Catch: " + e.getMessage());
        }
    }

    /**
     * Service that analyzes the metrics of the instance and generates alerts if necessary.
     * @param token
     * 
     * @return void
     * 
     * @see getAgentsArns
     * @see getQueuesArns
     * @see getRoutingProfilesArns
     * @see getMetricsById
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeMetrics(Short connectionId, InformationMetricSectionListDTO metrics, String resourceType, String resourceArn) {
        // Iterate over the metric sections and analyze them.
        metrics.getSections().forEach(
            section -> {
                Double sectionValue = section.getSectionValue();
                Double sectionParentValue = section.getSectionParentValue();

                if(sectionValue != null) {
                    switch (section.getSectionTitle()) {
                        case "ABANDONMENT_RATE":
                            analyzeAbandonmentRate(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            break;
                        case "AGENT_SCHEDULE_ADHERENCE":
                            analyzeAgentScheduleAdherence(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            break;
                        case "AVG_HANDLE_TIME":
                            analyzeAvgHandleTimeTraining(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            analyzeAvgHandleTimeIntervene(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            break;
                        case "AVG_QUEUE_ANSWER_TIME":
                            analyzeAvgQueueAnswerTime(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            break;
                        case "AVG_RESOLUTION_TIME":
                            // TODO: Implement this case
                            break;
                        case "PERCENT_CASES_FIRST_CONTACT_RESOLVED":
                            // TODO: Implement this case
                            break;
                        case "SERVICE_LEVEL":
                            analyzeServiceLevel(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            break;
                        case "AGENT_OCCUPANCY":
                            analyzeAgentOccupancy(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                            break;
                        default:
                            break;
                    }
                }
            }
        );
    }

    /**
     * Service that checks if a given alert is already in the database.
     * 
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return Boolean
     * 
     * @author Moisés Adame
     * 
     */
    public Boolean checkAlertExists(String resourceArn, Short insightId) {
        Collection<Alert> alerts = ((Collection<Alert>) alertService.checkAlertExists(resourceArn, insightId));
        return alerts.size() > 0;
    }

    /**
     * Service that analyzes AVG_HANDLE_TIME metric and generates training alerts if necessary.
     * 
     * @param connectionId
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeAvgHandleTimeTraining(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 10;
        Short trainingId = 1;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsBiggerBy10Percent = sectionValue > sectionParentValue * 1.1;

        if(valueIsBiggerBy10Percent && alertDoestExist) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);
            AlertDTO alertDTO = AlertDTO.newTrainingAlertDTO(connectionId, insightId, trainingId, resourceArn);
            
            try {
                alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
                String message = "Training Alert: Average handle time exceeded by 10% for " + resourceArn;
                messagingTemplate.convertAndSend("/topic/alertas", message); 
            } catch (Exception e) {
                System.out.println("Error in analyzeAvgHandleTimeTraining(), " + e.getMessage());
            }
        }
    }

    /**
     * Service that analyzes AGENT_SCHEDULE_ADHERENCE metric and generates alerts if necessary.
     * 
     * @param connectionId
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeAgentScheduleAdherence(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 11;
        Short trainingId = 2;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsLowerThan90Percent = sectionValue < 90;
    
        if (valueIsLowerThan90Percent && alertDoestExist) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);
            AlertDTO alertDTO = AlertDTO.newTrainingAlertDTO(connectionId, insightId, trainingId, resourceArn);
    
            try {
                alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
    
                // --- Add WebSocket Notification ---
                String message = "Agent Schedule Adherence Alert: Agent adherence is below 90% for " + resourceArn;
                messagingTemplate.convertAndSend("/topic/alertas", message);
                // ---------------------------------
    
            } catch (Exception e) {
                System.out.println("Error in analyzeAgentScheduleAdherence(), " + e.getMessage());
            }
        }
    }

    /**
     * Service that analyzes AVG_QUEUE_ANSWER_TIME metric and generates alerts if necessary.
     * 
     * @param connectionId
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeAvgQueueAnswerTime(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 12;
        Short trainingId = 3;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsBiggerBy10Percent = sectionValue > sectionParentValue * 1.1;
    
        if (!resourceType.equals("AGENT") && valueIsBiggerBy10Percent && alertDoestExist) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);
            AlertDTO alertDTO = AlertDTO.newTrainingAlertDTO(connectionId, insightId, trainingId, resourceArn);
            try {
                alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
    
                // --- Add WebSocket Notification ---
                String message = "Avg. Queue Answer Time Alert: Exceeded threshold by 10% for " + resourceArn;
                messagingTemplate.convertAndSend("/topic/alertas", message);
                // ---------------------------------
    
            } catch (Exception e) {
                System.out.println("Error in analyzeAvgQueueAnswerTime(), " + e.getMessage());
            }
        }
    }

    /**
     * Service that analyzes SERVICE_LEVEL metric and generates alerts if necessary.
     * 
     * @param connectionId
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeServiceLevel(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 14;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsLowerThan80Percent = sectionValue < 80;
    
        if (resourceType.equals("ROUTING_PROFILE") && valueIsLowerThan80Percent && alertDoestExist) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);
    
            // This information is obtained when the accept button is clicked
            String originalRoutingProfile = null;
            String transferedAgent = null;
    
            // The destination routing profile is the one that raised the alert.
            String destinationRoutingProfile = resourceArn;
    
            AlertDTO alertDTO = AlertDTO.newTransferAlertDTO(connectionId, insightId, originalRoutingProfile, destinationRoutingProfile, transferedAgent, resourceArn);
    
            try {
                alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
    
                // --- Add WebSocket Notification ---
                String message = "Service Level Alert: Service level below 80% for routing profile: " + resourceArn;
                messagingTemplate.convertAndSend("/topic/alertas", message);
                // ---------------------------------
    
            } catch (Exception e) {
                System.out.println("Error in analyzeServiceLevel(), " + e.getMessage());
            }
        }
    }

    /**
     * Service that analyzes ABANDONMENT_RATE metric and generates alerts if necessary.
     * 
     * @param connectionId
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeAbandonmentRate(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 15;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsBiggerThan5Percent = sectionValue > 5;

        if(resourceType.equals("ROUTING_PROFILE") && valueIsBiggerThan5Percent && alertDoestExist) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);

            // This information is obtained when the accept button is clicked
            String originalRoutingProfile = null;
            String transferedAgent = null;

            // The destination routing profile is the one that raised the alert.
            String destinationRoutingProfile = resourceArn;

            AlertDTO alertDTO = AlertDTO.newTransferAlertDTO(connectionId, insightId, originalRoutingProfile, destinationRoutingProfile, transferedAgent, resourceArn);

            try {
                alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
    
                // --- Add WebSocket Notification ---
                String message = "Abandonment Rate Alert: Abandonment rate exceeds 5% for routing profile: " + resourceArn;
                messagingTemplate.convertAndSend("/topic/alertas", message);
                // ---------------------------------
    
            } catch (Exception e) {
                System.out.println("Error in analyzeAbandonmentRate(), " + e.getMessage());
            }
        }
    }

        /**
     * Service that analyzes AGENT_OCCUPANCY metric and generates alerts if necessary.
     * 
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeAgentOccupancy(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 16;

        if (resourceType.equals("ROUTING_PROFILE") && sectionValue > 0.8 && !checkAlertExists(resourceArn, insightId)) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);

            // This information is obtained when the accept button is clicked
            String originalRoutingProfile = null;
            String transferedAgent = null;

            // The destination routing profile is the one that raised the alert.
            String destinationRoutingProfile = resourceArn;

            AlertDTO alertDTO = AlertDTO.newTransferAlertDTO(connectionId, insightId, originalRoutingProfile, destinationRoutingProfile, transferedAgent, resourceArn);

            try {
                alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
    
                // --- Add WebSocket Notification ---
                String message = "Agent Occupancy Alert: Occupancy above " + (sectionValue * 100) + "% for routing profile: " + resourceArn;
                messagingTemplate.convertAndSend("/topic/alertas", message);
                // ---------------------------------
    
            } catch (Exception e) {
                System.out.println("Error in analyzeAgentOccupancy(), " + e.getMessage());
            }
    
        }
    }

    /**
     * Service that analyzes AVG_HANDLE_TIME metric and generates intervene alerts if necessary.
     * 
     * @param connectionId
     * @param sectionValue
     * @param sectionParentValue
     * @param resourceType
     * @param resourceArn
     * 
     * @return void
     * 
     * @see checkAlertExists
     * @see AlertDTO
     * @see alertService
     * @see Alert
     * 
     * @author Moisés Adame
     * 
     */
    public void analyzeAvgHandleTimeIntervene(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 13;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsBiggerBy50Percent = sectionValue > sectionParentValue * 1.5;
    
        if (resourceType.equals("AGENT") && valueIsBiggerBy50Percent && alertDoestExist) {
            ConnectClientInfo clientInfo = getConnectClientInfoByIdentifier((int) connectionId);
    
            Set<String> problematicContacts = agentService.getNegativeSentimentContacts(clientInfo.getUid(), resourceArn);
    
            if (problematicContacts.size() > 0) {
                problematicContacts.forEach(contact -> {
                    AlertDTO alertDTO = AlertDTO.newInterventionAlertDTO(connectionId, insightId, contact, clientInfo.getSupervisor(), resourceArn);
                    try {
                        alertService.saveAlert(clientInfo.getUid(), alertService.fromDTO(alertDTO));
    
                        // --- Add WebSocket Notification ---
                        String message = "Avg. Handle Time (Intervene) Alert: Agent " + resourceArn + " has exceeded avg. handle time by 50% on contact " + contact;
                        messagingTemplate.convertAndSend("/topic/alertas", message);
                        // ---------------------------------
    
                    } catch (BadRequestException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
    public void sendWebSocketMessage(String message) {
        System.out.println("WebSocket message sent: " + message);
    }
}
