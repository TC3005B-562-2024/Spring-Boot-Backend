package tc3005b224.amazonconnectinsights.models_sql;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;
import tc3005b224.amazonconnectinsights.dto.insights.InsightDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "insight")
public class Insight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private int identifier;

    @ManyToOne
    @JoinColumn(name = "category_identifier", referencedColumnName = "identifier")
    private Category category;

    @Column(name = "denomination", nullable = false)
    private String denomination;

    @Column(name = "description", columnDefinition = "TINYTEXT")
    private String description;

    @Column(name = "date_registered", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date dateRegistered;

    @Column(name = "date_updated", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "insight", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alert> alert;

    // Constructors, getters, and setters
    public Insight() {
    }

    public Insight(Category category, String denomination, String description, boolean isActive) {
        this.category = category;
        this.denomination = denomination;
        this.description = description;
        this.isActive = isActive;
    }

    public Insight(InsightDTO insightDTO, Category category) {
        this.category = category;
        this.denomination = insightDTO.getDenomination();
        this.description = insightDTO.getDescription();
        this.dateRegistered = new Date();
        this.dateUpdated = new Date();
        this.isActive = insightDTO.getActive();
    }

    public void updateFromDTO(InsightDTO insightDTO){
        this.dateUpdated = new Date();

        if(insightDTO.getDenomination() != null){
            this.denomination = insightDTO.getDenomination();
        }

        if(insightDTO.getDescription() != null){
            this.description = insightDTO.getDescription();
        }

        if(insightDTO.getActive() != null){
            this.isActive = insightDTO.getActive();
        }
    }

    // Getters and setters
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getActive() {
        return isActive;
    }

    public List<Alert> getAlert() {
        return alert;
    }

    public void setAlert(List<Alert> alert) {
        this.alert = alert;
    }
}
