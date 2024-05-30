package tc3005b224.amazonconnectinsights.dto.skill;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationMetricSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.information.SkillsInformationDTO;
import tc3005b224.amazonconnectinsights.dto.queue.QueueCardDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingProgressItemDTO;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SkillDTO {
    private String id;
    private String arn;
    private String alias;
    private Long totalAgents;
    private Iterable<QueueCardDTO> queues;
    private AlertPriorityDTO alerts;
    private SkillsInformationDTO skillsInformationDTO;
    private List<TrainingProgressItemDTO> trainings;
    private InformationMetricSectionListDTO metrics;
    private Iterable<AgentCardDTO> agents;
}
