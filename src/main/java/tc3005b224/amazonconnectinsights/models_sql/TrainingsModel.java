package tc3005b224.amazonconnectinsights.models_sql;

import java.util.Date;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Trainings")
public class TrainingsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private Long identifier;

    @Column(name = "denomination", nullable = false)
    private String denomination;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "date_registered", nullable = false)
    private Date dateRegistered;

    @Column(name = "date_updated", nullable = false)
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Alert_identifier", referencedColumnName = "identifier")
    private Alert alert;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
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

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }
}
