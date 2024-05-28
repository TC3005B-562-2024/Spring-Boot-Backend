package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileRequest;
import software.amazon.awssdk.services.connect.model.DescribeRoutingProfileResponse;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesRequest;
import software.amazon.awssdk.services.connect.model.ListRoutingProfilesResponse;
import software.amazon.awssdk.services.connect.model.RoutingProfile;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.information.SkillsInformationDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Service
public class SkillService extends BaseService {
    @Autowired
    private AlertService alertService;

    @Autowired
    private MetricService metricService;

    @Autowired
    private TrainingsService trainingsService;

    @Autowired
    private AgentService agentService;
    
    // Service that calls amazon connects ListRoutingProfiles and returns a list of SkillBriefDO
    public List<SkillBriefDTO> findByInstance(String token) {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        // Build the request for the ListRoutingProfiles endpoint using the instanceId
        ListRoutingProfilesRequest listRoutingProfilesRequest = ListRoutingProfilesRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .build();
        // The response of the ListRoutingProfiles endpoint
        ListRoutingProfilesResponse listRoutingProfilesResponse = getConnectClient(clientInfo.getAccessKeyId(),
        clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .listRoutingProfiles(listRoutingProfilesRequest);
        // The list of Routing Profiles, excluding the nextToken, hopefully the list is not too long.
        // If it is, we will need to implement pagination.
        List<RoutingProfileSummary> data = listRoutingProfilesResponse.routingProfileSummaryList();
        // Build the result as a list of SkillBriefDTO
        List<SkillBriefDTO> result = new ArrayList<SkillBriefDTO>();
        data.forEach(routingProfileSummary -> {
            result.add(new SkillBriefDTO(routingProfileSummary.id(), routingProfileSummary.arn(),
                    routingProfileSummary.name(), "phone_in_talk"));
        });
        return result;
    }

    // Service that retrieves the Skill (Routing Profile) information given its skillId.
    public SkillDTO findById(String token, String skillId) throws BadRequestException {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(token);
        DescribeRoutingProfileRequest describeRoutingProfileRequest = DescribeRoutingProfileRequest.builder()
                .instanceId(clientInfo.getInstanceId())
                .routingProfileId(skillId)
                .build();
        DescribeRoutingProfileResponse describeRoutingProfileResponse = getConnectClient(clientInfo.getAccessKeyId(),
                clientInfo.getSecretAccessKey(), clientInfo.getRegion())
                .describeRoutingProfile(describeRoutingProfileRequest);
        RoutingProfile data = describeRoutingProfileResponse.routingProfile();
        AlertPriorityDTO alerts = alertService.findByResource(clientInfo.getConnectionIdentifier(), data.routingProfileArn());

        Instant createdAt = Instant.now(); // TODO: Change Mocked creation time
        SkillsInformationDTO skillsInformationDTO = new SkillsInformationDTO(data.name(), createdAt, data.numberOfAssociatedUsers());

        InformationSectionListDTO metrics = metricService.getMetricsById(token, "ROUTING_PROFILE", data.routingProfileArn());
        
        Iterable<Alert> trainings = trainingsService
                .findTrainingsAlertsByResource(clientInfo.getConnectionIdentifier(), data.routingProfileArn());
        
        Iterable<AgentCardDTO> agents = agentService.findAll(token, skillId);
                
        SkillDTO response = new SkillDTO(
        skillId,
        data.routingProfileArn(),
        data.name(),
        data.numberOfAssociatedUsers(),
        null,
        alerts,
        skillsInformationDTO,
        trainings,
        metrics,
        agents);
        
        return response;
    }
}
