package tc3005b224.amazonconnectinsights.dto.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgentAvailableToTransferListDTO {
    private String cappedRoutingProfileId;
    private Iterable<AgentMinimalDTO> agents;
}
