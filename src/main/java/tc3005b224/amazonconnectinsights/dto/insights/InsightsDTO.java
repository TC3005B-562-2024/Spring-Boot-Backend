package tc3005b224.amazonconnectinsights.dto.insights;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class InsightsDTO {
    private short identifier;
    private short categoryIdentifier;
    private String denomination;
    private String description;
    private Date dateRegistered;
    private Date dateUpdated;
    private boolean isActive;

    public boolean getActive() {
        return isActive;
    }
}


