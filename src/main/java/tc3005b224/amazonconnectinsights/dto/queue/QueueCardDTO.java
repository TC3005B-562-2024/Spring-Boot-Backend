package tc3005b224.amazonconnectinsights.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueueCardDTO {
    private String id;
    private String arn;
    private String name;
    private String description;
    private String status;
    private Integer maxContacts;
    private Integer totalAgents;
}
