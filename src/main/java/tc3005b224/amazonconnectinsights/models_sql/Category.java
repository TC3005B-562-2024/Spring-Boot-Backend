package tc3005b224.amazonconnectinsights.models_sql;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    private Short identifier;

    @Column(name = "denomination", nullable = false)
    private String denomination;

    @Column(name = "description", columnDefinition = "TINYTEXT")
    private String description;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "date_registered", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date dateRegistered;

    @Column(name = "date_updated", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date dateUpdated;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Insight> insights;

    // Constructors, getters, and setters
    public Category(String denomination, String description, Short priority, Boolean isActive) {
        this.denomination = denomination;
        this.description = description;
        this.priority = priority;
        this.isActive = isActive;
    }

    public Category() {
        ;
    }

    public Category(CategoryDTO categoryDTO) {
        this.denomination = categoryDTO.getDenomination();
        this.description = categoryDTO.getDescription();
        this.priority = categoryDTO.getPriority();
        this.dateRegistered = new Date();
        this.dateUpdated = new Date();
        this.isActive = categoryDTO.getActive();
    }

    public void updateFromDTO(CategoryDTO categoryDTO){
        this.dateUpdated = new Date();

        if(categoryDTO.getDenomination() != null){
            this.denomination = categoryDTO.getDenomination();
        }

        if(categoryDTO.getDescription() != null){
            this.description = categoryDTO.getDescription();
        }

        if(categoryDTO.getPriority() != null){
            this.priority = categoryDTO.getPriority();
        }

        if(categoryDTO.getActive() != null){
            this.isActive = categoryDTO.getActive();
        }
    }
    
    // Getters and setters
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getActive() {
        return isActive;
    }

    public List<Insight> getInsights() {
        return insights;
    }

    public void setInsights(List<Insight> insights) {
        this.insights = insights;
    }
}

