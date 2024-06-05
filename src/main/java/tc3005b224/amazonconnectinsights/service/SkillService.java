package tc3005b224.amazonconnectinsights.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.connect.model.RoutingProfile;
import software.amazon.awssdk.services.connect.model.RoutingProfileSummary;
import software.amazon.awssdk.services.connect.model.UserData;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.information.SkillsInformationDTO;
import tc3005b224.amazonconnectinsights.dto.queue.QueueCardDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingProgressItemDTO;
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

    @Autowired
    private QueueService queueService;
    
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

        Iterable<QueueCardDTO> queuesData = queueService.findAll(userUuid, data.routingProfileArn());

        SkillDTO response = new SkillDTO(
            skillId,
            data.routingProfileArn(),
            data.name(),
            data.numberOfAssociatedUsers(),
            queuesData,
            alerts,
            skillsInformationDTO,
            getTrainings(userUuid, data.routingProfileArn(), queuesData),
            metrics,
            agents
        );
        
        return response;
    }

    /**
     * Get trainings of a specific skill.
     * 
     * @param userUuid
     * @param skillArn
     * @param queues
     * 
     * @return List<TrainingProgressItemDTO>
     * 
     * @author Moisés Adame
     * 
     */
    public List<TrainingProgressItemDTO> getTrainings(String userUuid, String skillArn, Iterable<QueueCardDTO> queues) {
        // Create a new list of TrainingProgressItemDTO
        List<TrainingProgressItemDTO> trainings = new ArrayList<TrainingProgressItemDTO>();

        // Map of total trainings per agent.
        Map<String, Integer> totalTrainings = new HashMap<String, Integer>();
        Map<String, Float> trainingProgress = new HashMap<String, Float>();

        // Insntiate a client info.
        ConnectClientInfo clientInfo = getConnectClientInfo(userUuid);

        // Iterate over the queues and get the total trainings per agent.
        queues.forEach(
            queue -> {
                List<UserData> agents = queueService.findAgentsInQueue(userUuid, queue.getId());
                List<TrainingProgressItemDTO> queueTrainings = queueService.getTrainings(userUuid, queue.getArn(), agents);

                queueTrainings.forEach(
                    training -> {
                        if (trainingProgress.containsKey(queue.getName())) {
                            trainingProgress.put(queue.getName(), trainingProgress.get(queue.getName()) + training.getResourceTrainingProgress() / agents.size());
                        } else {
                            trainingProgress.put(queue.getName(), training.getResourceTrainingProgress() / agents.size());
                        }
                    }
                );

                TrainingProgressItemDTO training = new TrainingProgressItemDTO(queue.getName(), trainingProgress.get(queue.getName()));
                trainings.add(training);
            }
        );
        
        return trainings;
    }
}
