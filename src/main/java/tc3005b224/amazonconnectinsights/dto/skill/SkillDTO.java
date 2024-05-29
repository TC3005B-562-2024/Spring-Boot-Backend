package tc3005b224.amazonconnectinsights.dto.skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.InformationSectionListDTO;
import tc3005b224.amazonconnectinsights.dto.information.SkillsInformationDTO;
import tc3005b224.amazonconnectinsights.dto.utils.IdAndNameDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SkillDTO {
    private String id;
    private String arn;
    private String alias;
    private Long totalAgents;
    private Iterable<IdAndNameDTO> queues;
    private AlertPriorityDTO alerts;
    private SkillsInformationDTO skillsInformationDTO;
    private Iterable<Alert> trainings;
    private InformationSectionListDTO metrics;
    private Iterable<AgentCardDTO> agents;
}
