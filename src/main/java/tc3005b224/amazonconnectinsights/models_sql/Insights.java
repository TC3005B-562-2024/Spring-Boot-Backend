package tc3005b224.amazonconnectinsights.models_sql;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Insights")
public class Insights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short identifier;

    @Column(name = "Category_identifier", nullable = false)
    private byte categoryIdentifier;

    @Column(name = "denomination", nullable = false)
    private String denomination;

    @Column(name = "date_registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateRegistered = LocalDateTime.now();

    @Column(name = "date_updated", nullable = false, columnDefinition = "TIMESTAMO DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateUpdated = LocalDateTime.now();

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive = true;

    public Short getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Short identifier) {
        this.identifier = identifier;
    }

    public byte getCategoryIdentifier() {
        return categoryIdentifier;
    }

    public void setCategoryIdentifier(byte categoryIdentifier) {
        this.categoryIdentifier = categoryIdentifier;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
