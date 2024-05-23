package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.ListUsersRequest;
import software.amazon.awssdk.services.connect.model.ListUsersResponse;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import software.amazon.awssdk.services.connect.model.ListQueuesRequest;
import software.amazon.awssdk.services.connect.model.ListQueuesResponse;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;

@Service
public class AlertEngineService extends BaseService {
    @Autowired
    private MetricService metricService;

    public void sendAlert(String message) {
        try {
            List<String> routingProfiles = getRoutingProfilesArns("token");
            String queues = getQueuesArns("token").toString();
            String agents = getAgentsArns("token").toString();

            System.out.println("Routing Profiles: ");
            metricService.getMetricsByList("token", "ROUTING_PROFILE", routingProfiles);
            System.out.println(routingProfiles);
            System.out.println("Queues: ");
            System.out.println(queues);
            System.out.println("Agents: ");
            System.out.println(agents);
            
            // metricService.getMetricsById("token", "AGENT", "arn:aws:connect:us-east-1:674530197385:instance/7c78bd60-4a9f-40e5-b461-b7a0dfaad848/agent/6887b106-f684-485e-9c47-a6b1e16cdd21");
        } catch (Exception e) {
            System.out.println("Catch: " + e.getMessage());
        }
    }

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
    public List<String> getQueuesArns(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);

        // Get the queues
        ListQueuesResponse queues = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).listQueues(
            ListQueuesRequest
                .builder()
                .instanceId(clientInfo.getInstanceId())
                .build()
        );

        // Instantiate the list of queues
        List<String> queuesArn = new ArrayList<>();

        // Add the arn's to the list
        queues.queueSummaryList().forEach(queue -> {
            queuesArn.add(queue.arn());
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
            List<String> queues = getQueuesArns(token);
            List<String> agents = getAgentsArns(token);

            

            routingProfiles.forEach(
                routingProfileArn -> {
                    try {
                        InformationMetricSectionListDTO metrics = metricService.getMetricsById(token, "ROUTING_PROFILE", routingProfileArn);
                        System.out.println("Routing Profile: " + routingProfileArn);
                        analyzeMetrics(metrics, routingProfileArn);
                    } catch (BadRequestException e) {
                        System.out.println("Catch: " + e.getMessage());
                    }
                }
            );
        } catch (Exception e) {
            System.out.println("Catch: " + e.getMessage());
        }
    }

    public void analyzeMetrics(InformationMetricSectionListDTO metrics, String resource) {
        // Iterate over the metric sections and analyze them.
        metrics.getSections().forEach(
            section -> {
                Double sectionValue = section.getSectionValue();
                switch (section.getSectionTitle()) {
                    case "ABANDONMENT_RATE":
                        System.out.println("ABANDONMENT_RATE" + sectionValue);
                        break;
                    case "AGENT_SCHEDULE_ADHERENCE":
                        System.out.println("agen: " + sectionValue);
                        break;
                    case "AVG_QUEUE_ANSWER_TIME":
                        System.out.println("AGENT_SCHEDULE_ADHERENCE" + sectionValue);
                        break;
                    case "AVG_RESOLUTION_TIME":
                        System.out.println("AVG_RESOLUTION_TIME" + sectionValue);
                        break;
                    case "PERCENT_CASES_FIRST_CONTACT_RESOLVED":
                        System.out.println("PERCENT_CASES_FIRST_CONTACT_RESOLVED" + sectionValue);
                        break;
                    case "SERVICE_LEVEL":
                        System.out.println("SERVICE_LEVEL" + sectionValue);
                        break;
                    case "AGENT_OCCUPANCY":
                        System.out.println("AGENT_OCCUPANCY" + sectionValue);
                        break;
                    default:
                        break;
                }
            }
        );
    }
}
