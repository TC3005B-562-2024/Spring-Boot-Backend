package tc3005b224.amazonconnectinsights.dto.insights;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class InsightDTO {
    private short categoryIdentifier;
    private String denomination;
    private String description;
    private Boolean isActive;

    public short getCategoryIdentifier() {
        return categoryIdentifier;
    }

    public void setCategoryIdentifier(short categoryIdentifier) {
        this.categoryIdentifier = categoryIdentifier;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}


