package tc3005b224.amazonconnectinsights.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.ListUsersRequest;
import software.amazon.awssdk.services.connect.model.ListUsersResponse;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import software.amazon.awssdk.services.connect.model.ListQueuesRequest;
import software.amazon.awssdk.services.connect.model.ListQueuesResponse;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;

@Service
public class AlertEngineService extends BaseService {
    @Autowired
    private MetricService metricService;

    @Autowired
    private AlertService alertService;

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

            ConnectClientInfo clientInfo = getConnectClientInfo(token);

            routingProfiles.forEach(
                routingProfileArn -> {
                    try {
                        InformationMetricSectionListDTO metrics = metricService.getMetricsById(token, "ROUTING_PROFILE", routingProfileArn);
                        System.out.println("Routing Profile: " + routingProfileArn);
                        analyzeMetrics(clientInfo.getConnectionIdentifier(), metrics, "ROUTING_PROFILE", routingProfileArn);
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

                switch (section.getSectionTitle()) {
                    case "ABANDONMENT_RATE":
                        analyzeAbandonmentRate(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                        break;
                    case "AGENT_SCHEDULE_ADHERENCE":
                        analyzeAgentScheduleAdherence(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                        break;
                    case "AVG_HANDLE_TIME":
                        analyzeAvgHandleTime(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
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
                        analyzeAgentOccupancy(connectionId, sectionValue, sectionParentValue, resourceType, resourceArn);
                        break;
                    default:
                        break;
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
            // TODO: Get agent whose status is "Available" and doesn't belong to the problematic routing profile
            AlertDTO alertDto = new AlertDTO(connectionId, insightId, null, resourceArn, null, null);
            alertService.saveAlert(alertService.fromDTO(alertDto));
        }
    }

    /**
     * Service that analyzes AVG_HANDLE_TIME metric and generates alerts if necessary.
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
    public void analyzeAvgHandleTime(Short connectionId, Double sectionValue, Double sectionParentValue, String resourceType, String resourceArn) {
        Short insightId = 10;
        Short trainingId = 1;
        Boolean alertDoestExist = !checkAlertExists(resourceArn, insightId);
        Boolean valueIsBiggerBy10Percent = sectionValue > sectionParentValue * 1.1;

        if(valueIsBiggerBy10Percent && alertDoestExist) {
            AlertDTO alertDto = new AlertDTO(connectionId, insightId, trainingId, resourceArn, null, null);
            alertService.saveAlert(alertService.fromDTO(alertDto));
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

        if(valueIsLowerThan90Percent && alertDoestExist) {
            AlertDTO alertDto = new AlertDTO(connectionId, insightId, trainingId, resourceArn, null, null);
            alertService.saveAlert(alertService.fromDTO(alertDto));
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
            // TODO: Get agent whose status is "Available" and doesn't belong to the problematic routing profile
            AlertDTO alertDto = new AlertDTO(connectionId, insightId, null, resourceArn, null, null);
            alertService.saveAlert(alertService.fromDTO(alertDto));
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

        if(resourceType.equals("ROUTING_PROFILE") && valueIsLowerThan80Percent && alertDoestExist) {
            // TODO: Get agent whose status is "Available" and doesn't belong to the problematic routing profile.
            AlertDTO alertDto = new AlertDTO(connectionId, insightId, null, resourceArn, null, null);
            alertService.saveAlert(alertService.fromDTO(alertDto));
        }
    }
}
