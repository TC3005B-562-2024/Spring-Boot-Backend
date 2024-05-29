package tc3005b224.amazonconnectinsights.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private String arn;
    private String contactId;
    private String initiationMethod;
    private String channel;
    private String queueId;
    private String enqueueTimestamp;
    private String agentId;
    private String agentConnectTimestamp;
    private String agentDisconnectTimestamp;
}
