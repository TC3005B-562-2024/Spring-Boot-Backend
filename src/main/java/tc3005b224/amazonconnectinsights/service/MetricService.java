package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.FilterV2;
import software.amazon.awssdk.services.connect.model.GetMetricDataV2Request;
import software.amazon.awssdk.services.connect.model.GetMetricDataV2Response;
import software.amazon.awssdk.services.connect.model.MetricV2;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionDTO;
import software.amazon.awssdk.services.connect.model.ThresholdV2;

@Service
public class MetricService extends BaseService {
    public InformationSectionListDTO getMetricsById(String token, String resourceType, String resourceArn) throws BadRequestException {
        // Check if the resource type is valid.
        if(!resourceType.equals("AGENT") && !resourceType.equals("QUEUE")){
            throw new BadRequestException("Invalid resource type");
        }
        
        // Metrics to search.
        Collection<MetricV2> metricsToSearch = new ArrayList<>();
        metricsToSearch.add(MetricV2.builder().name("ABANDONMENT_RATE").build());
        metricsToSearch.add(MetricV2.builder().name("AGENT_SCHEDULE_ADHERENCE").build());
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
                .resourceArn(clientInfo.getInstanceArn())
                .build()
        );

        System.out.println(metrics.metricResults().toString());

        // Create the information section list.
        InformationSectionListDTO informationSectionListDTO = new InformationSectionListDTO();
        informationSectionListDTO.setSectionTitle("Metrics");
        
        // Create list of sections
        List<InformationSectionDTO> sections = new ArrayList<>();
        sections.add(new InformationSectionDTO("Abandonment Rate", metrics.metricResults().get(0).collections().get(0).value().toString(), "green"));
        sections.add(new InformationSectionDTO("Agent Schedule Adherence", metrics.metricResults().get(1).collections().get(0).value().toString(), "green"));
        sections.add(new InformationSectionDTO("Average Queue Answer Time", metrics.metricResults().get(2).collections().get(0).value().toString(), "green"));
        sections.add(new InformationSectionDTO("Average Resolution Time", metrics.metricResults().get(3).collections().get(0).value().toString(), "green"));
        sections.add(new InformationSectionDTO("Percent Cases First Contact Resolved", metrics.metricResults().get(4).collections().get(0).value().toString(), "green"));
        sections.add(new InformationSectionDTO("Service Level", metrics.metricResults().get(5).collections().get(0).value().toString(), "green"));
        sections.add(new InformationSectionDTO("Agent Occupancy", metrics.metricResults().get(6).collections().get(0).value().toString(), "green"));

        // Add secrtions to the information section list.
        informationSectionListDTO.setSections(sections);

        System.out.println(metrics);

        return informationSectionListDTO;
    }
}
