package tc3005b224.amazonconnectinsights.models_sql;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "insight")
public class Insight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short identifier;

    @Column(name = "denomination", nullable = false, length = 100)
    private String denomination;

    @Column(name = "description", nullable = true, length = 8)
    private String description;

    @Column(name = "date_registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateRegistered;

    @Column(name = "date_updated", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "insight")
    List<Alert> alerts;

    @ManyToOne
    @JoinColumn(name = "category_identifier", nullable = false)
    private Category category;

    public Short getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Short identifier) {
        this.identifier = identifier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategories() {
        return category;
    }

    public void setCategories(Category category) {
        this.category = category;
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

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
}
