package tc3005b224.amazonconnectinsights.dto.category;

public class CategoryDTO {

    private String denomination;
    private String description;
    private Short priority;
    private Boolean isActive;

    public CategoryDTO(String denomination, String description, Short priority, Boolean isActive) {
        this.denomination = denomination;
        this.description = description;
        this.priority = priority;
        this.isActive = isActive;
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

    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
