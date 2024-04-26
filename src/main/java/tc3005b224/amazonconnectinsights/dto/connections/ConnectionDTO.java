package tc3005b224.amazonconnectinsights.dto.connections;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ConnectionDTO {
    private long identifier;
    private String denomination;
    private String description;
    private Date dateJoined;
    private Date dateUpdated;
    private Boolean isActive;
}