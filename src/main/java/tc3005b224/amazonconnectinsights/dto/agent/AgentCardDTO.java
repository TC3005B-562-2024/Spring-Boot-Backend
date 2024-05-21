package tc3005b224.amazonconnectinsights.dto.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentCardDTO {
    private String id;
    private String name;
    private String status;
    private String emotion;
    private Iterable<String> skills;
    private String highestPriorityAlert;
}
