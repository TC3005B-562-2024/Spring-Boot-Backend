package tc3005b224.amazonconnectinsights.models_sql;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "training")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private Short identifier;

    @Column(name = "denomination", nullable = false, length = 100)
    private String denomination;

    @Column(name = "description", nullable = true, length = 8)
    private String description;

    @Column(name = "date_registered", nullable = false)
    private Date dateRegistered;

    @Column(name = "date_updated", nullable = false)
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "training")
    private List<Alert> alerts;

    public Short getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Short identifier) {
        this.identifier = identifier;
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

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
}
