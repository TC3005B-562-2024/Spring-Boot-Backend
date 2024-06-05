package tc3005b224.amazonconnectinsights.dto.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tc3005b224.amazonconnectinsights.dto.queue.QueueMinDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentCardDTO {
    private String id;
    private String arn;
    private String name;
    private String status;
    private String sentiment;
    private Iterable<QueueMinDTO> queues;
    private String topPriorityAlert;
}
