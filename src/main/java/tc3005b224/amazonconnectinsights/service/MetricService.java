package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeUserRequest;
import software.amazon.awssdk.services.connect.model.DescribeUserResponse;
import software.amazon.awssdk.services.connect.model.FilterV2;
import software.amazon.awssdk.services.connect.model.GetMetricDataV2Request;
import software.amazon.awssdk.services.connect.model.GetMetricDataV2Response;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfileQueuesResponse;
import software.amazon.awssdk.services.connect.model.MetricV2;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import software.amazon.awssdk.services.connect.model.ThresholdV2;

@Service
public class MetricService extends BaseService {
    @Autowired
    private InstanceService instanceService;

    public InformationMetricSectionListDTO getMetricsById(String token, String resourceType, String resourceArn) throws BadRequestException {
        // Check if the resource type is valid.
        if(!resourceType.equals("AGENT") && !resourceType.equals("QUEUE") && !resourceType.equals("ROUTING_PROFILE")){
            throw new BadRequestException("Invalid resource type");
        }

        String resourceParentType = "ROUTING_PROFILE";
        if(resourceType.equals("AGENT")){
            resourceParentType = "QUEUE";
        }else if(resourceType.equals("QUEUE")){
            resourceParentType = "ROUTING_PROFILE";
        }
        
        // Metrics to search.
        Collection<MetricV2> metricsToSearch = new ArrayList<>();
        metricsToSearch.add(MetricV2.builder().name("ABANDONMENT_RATE").build());
        metricsToSearch.add(MetricV2.builder().name("AGENT_SCHEDULE_ADHERENCE").build());
        metricsToSearch.add(MetricV2.builder().name("AVG_HANDLE_TIME").build());
        metricsToSearch.add(MetricV2.builder().name("AVG_QUEUE_ANSWER_TIME").build());
        metricsToSearch.add(MetricV2.builder().name("AVG_RESOLUTION_TIME").build());
        metricsToSearch.add(MetricV2.builder().name("PERCENT_CASES_FIRST_CONTACT_RESOLVED").build());
        metricsToSearch.add(MetricV2.builder().name("SERVICE_LEVEL")
                .threshold(ThresholdV2.builder().thresholdValue(7200.0).comparison("LT").build()).build());

        if(resourceType.equals("AGENT")){
            metricsToSearch.add(MetricV2.builder().name("AGENT_OCCUPANCY").build());
        }

        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        GetMetricDataV2Response metrics = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).getMetricDataV2(
            GetMetricDataV2Request.builder()
                .startTime(Instant.now().minusSeconds(7200))
                .endTime(Instant.now())
                .filters(
                    FilterV2.builder()
                        .filterKey(resourceType)
                        .filterValues(resourceArn)
                        .build()
                )
                .metrics(
                    metricsToSearch
                )
                .resourceArn(instanceService.getInstanceDetails(token).getArn())
                .build()
        );

        // Get the metrics of the parent
        GetMetricDataV2Response parentMetrics;

        //  Create a map with the metrics of the parent
        Map<String, Double> parentMetricsMap = new HashMap<>();
        if(resourceType.equals("AGENT")){
            parentMetrics = getConnectClient(
                clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(),
                clientInfo.getRegion()
            ).getMetricDataV2(
                GetMetricDataV2Request.builder()
                    .startTime(Instant.now().minusSeconds(7200))
                    .endTime(Instant.now())
                    .filters(
                        FilterV2.builder()
                            .filterKey("QUEUE")
                            .filterValues(getResourceParentArns(token, resourceParentType, resourceArn))
                            .build()
                    )
                    .metrics(
                        metricsToSearch
                    )
                    .resourceArn(instanceService.getInstanceDetails(token).getArn())
                    .build()
            );

            parentMetrics.metricResults().forEach(metric -> {
                metric.collections().forEach(collection -> {
                    parentMetricsMap.put(collection.metric().name(), collection.value());
                });
            });
        }

        // Create the information section list.
        InformationMetricSectionListDTO informationSectionListDTO = new InformationMetricSectionListDTO();
        informationSectionListDTO.setSectionTitle("Metrics");

        // Create list of sections
        List<InformationMetricSectionDTO> sections = new ArrayList<>();
        if(metrics.metricResults().size() > 0){
            metrics.metricResults().forEach(metric -> {
                metric.collections().forEach(collection -> {
                    Double collectionValue = collection.value() == null ? null : collection.value();
                    String metricName = collection.metric().name();
                    Double parentMetricValue = null;
                    if(parentMetricsMap.get(metricName) != null){
                        parentMetricValue = parentMetricsMap.get(metricName);
                    }
                    InformationMetricSectionDTO section = new InformationMetricSectionDTO(metricName, collectionValue, parentMetricValue, "green");
                    sections.add(section);
                });
            });
        }

        informationSectionListDTO.setSections(sections);

        return informationSectionListDTO;
    }

    public void getMetricsByList(String token, String resourceType, List<String> resourceArns) throws BadRequestException {
        // Check if the resource type is valid.
        if(!resourceType.equals("AGENT") && !resourceType.equals("QUEUE") && !resourceType.equals("ROUTING_PROFILE")){
            throw new BadRequestException("Invalid resource type");
        }
        
        // Metrics to search.
        Collection<MetricV2> metricsToSearch = new ArrayList<>();
        metricsToSearch.add(MetricV2.builder().name("ABANDONMENT_RATE").build());
        metricsToSearch.add(MetricV2.builder().name("AGENT_SCHEDULE_ADHERENCE").build());
        metricsToSearch.add(MetricV2.builder().name("AVG_HANDLE_TIME").build());
        metricsToSearch.add(MetricV2.builder().name("AVG_QUEUE_ANSWER_TIME").build());
        metricsToSearch.add(MetricV2.builder().name("AVG_RESOLUTION_TIME").build());
        metricsToSearch.add(MetricV2.builder().name("PERCENT_CASES_FIRST_CONTACT_RESOLVED").build());
        metricsToSearch.add(MetricV2.builder().name("SERVICE_LEVEL")
                .threshold(ThresholdV2.builder().thresholdValue(7200.0).comparison("LT").build()).build());

        if(resourceType.equals("AGENT")){
            metricsToSearch.add(MetricV2.builder().name("AGENT_OCCUPANCY").build());
        }

        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        GetMetricDataV2Response metrics = getConnectClient(
            clientInfo.getAccessKeyId(),
            clientInfo.getSecretAccessKey(),
            clientInfo.getRegion()
        ).getMetricDataV2(
            GetMetricDataV2Request.builder()
                .startTime(Instant.now().minusSeconds(7200))
                .endTime(Instant.now())
                .filters(
                    FilterV2.builder()
                        .filterKey(resourceType)
                        .filterValues(resourceArns)
                        .build()
                )
                .metrics(
                    metricsToSearch
                )
                .resourceArn(instanceService.getInstanceDetails(token).getArn())
                .build()
        );
    }

    /**
     * Service that retrieves a list of all the arns of the parents 
     * of a given resource.
     * 
     * @param token
     * @param resourceType
     * @param resourceArn
     * @return List<String>
     * @throws BadRequestException
     * 
     * @see AgentCardDTO
     */
    public List<String> getResourceParentArns(String token, String resourceType, String resourceArn) throws BadRequestException {
        if(!resourceType.equals("AGENT") && 
           !resourceType.equals("QUEUE") && 
           !resourceType.equals("ROUTING_PROFILE")){
            throw new BadRequestException("Invalid resource type");
        }else{
            ConnectClientInfo clientInfo = getConnectClientInfo(token);
            List<String> queuesList = new ArrayList<>();

            if(resourceType.equals("AGENT")){                
                // Get the agent general information
                DescribeUserResponse agent = getConnectClient(
                    clientInfo.getAccessKeyId(), 
                    clientInfo.getSecretAccessKey(),
                    clientInfo.getRegion()
                ).describeUser(
                    DescribeUserRequest
                    .builder()
                    .instanceId(clientInfo.getInstanceId())
                    .userId(resourceArn)
                    .build()
                );
        
                // Get the queues associated to the agent
                ListRoutingProfileQueuesResponse routingProfileQueues = getConnectClient(
                    clientInfo.getAccessKeyId(),
                    clientInfo.getSecretAccessKey(), 
                    clientInfo.getRegion()
                ).listRoutingProfileQueues(
                    ListRoutingProfileQueuesRequest
                    .builder()
                    .instanceId(clientInfo.getInstanceId())
                    .routingProfileId(agent.user().routingProfileId()).build()
                );
        
                routingProfileQueues.routingProfileQueueConfigSummaryList().forEach(queue -> {
                    queuesList.add(queue.queueName());
                });
            }
            return queuesList;
        }
    }
}
