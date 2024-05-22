package tc3005b224.amazonconnectinsights.dto.skill;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SkillDTO {
    private String id;
    private String alias;
    private Instant createdAt;
    private Long totalAgents;
    private String serviceLevel;
    private String acr;
    private String asa;
    private String fcr;
    private String adherence;
    private AlertPriorityDTO alerts;
    // TODO: Revisar
    private Iterable<Alert> trainings;
    private Iterable<AgentDTO> agents;
}
