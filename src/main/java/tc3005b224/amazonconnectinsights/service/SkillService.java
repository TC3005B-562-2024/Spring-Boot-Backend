package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.RoutingProfile;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.information.SkillsInformationDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.dto.utils.IdAndNameDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Service
public class SkillService extends BaseService {
    @Autowired
    private RoutingProfileService routingProfileService;
        
    @Autowired
    private AlertService alertService;

    @Autowired
    private MetricService metricService;

    @Autowired
    private TrainingsService trainingsService;

    @Autowired
    private AgentService agentService;
    
    // Service that calls amazon connects ListRoutingProfiles and returns a list of SkillBriefDO
    public List<SkillBriefDTO> findByInstance(String userUuid) {
        // Get the list of Routing Profiles
        Iterable<RoutingProfileSummary> data = routingProfileService.getRoutingProfiles(userUuid);
        
        // Build the result as a list of SkillBriefDTO
        List<SkillBriefDTO> result = new ArrayList<SkillBriefDTO>();
        data.forEach(routingProfileSummary -> {
            result.add(new SkillBriefDTO(routingProfileSummary.id(), routingProfileSummary.arn(),
                    routingProfileSummary.name(), "phone_in_talk"));
        });
        return result;
    }

    // Service that retrieves the Skill (Routing Profile) information given its skillId.
    public SkillDTO findById(String userUuid, String skillId) throws BadRequestException {
        // Get the client info
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);
        RoutingProfile data = routingProfileService.getRoutingProfile(userUuid, skillId);
        AlertPriorityDTO alerts = alertService.findByResource(userUuid, data.routingProfileArn());

        Instant createdAt = Instant.now(); // TODO: Change Mocked creation time
        SkillsInformationDTO skillsInformationDTO = new SkillsInformationDTO(data.name(), createdAt, data.numberOfAssociatedUsers());

        InformationMetricSectionListDTO metrics = metricService.getMetricsById(userUuid, "ROUTING_PROFILE", data.routingProfileArn());
        
        Iterable<Alert> trainings = trainingsService
                .findTrainingsAlertsByResource(clientInfo.getIdentifier(), data.routingProfileArn());
        
        Iterable<AgentCardDTO> agents = agentService.findAll(userUuid, skillId);    
        
        Iterable<IdAndNameDTO> queues = new ArrayList<IdAndNameDTO>();

        SkillDTO response = new SkillDTO(
            skillId,
            data.routingProfileArn(),
            data.name(),
            data.numberOfAssociatedUsers(),
            queues,
            alerts,
            skillsInformationDTO,
            trainings,
            metrics,
            agents
        );
        
        return response;
    }
}
