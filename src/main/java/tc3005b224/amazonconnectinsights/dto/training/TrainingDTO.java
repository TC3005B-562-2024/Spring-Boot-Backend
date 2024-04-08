package tc3005b224.amazonconnectinsights.dto.training;

import java.util.Date;

/**
 * Data Transfer Object for Training entity information
 * 
 * @author Diego Jacobo Djmr5
 */
public class TrainingDTO {

    private String id;
    private String description;
    private String agentId;
    private Date dateOfCreation;
    private Date dateOfCompletion;
    private boolean isCompleted;

    public TrainingDTO() {
    }

    public TrainingDTO(String id, String description, String agentId, Date dateOfCreation, Date dateOfCompletion,
            boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.agentId = agentId;
        this.dateOfCreation = dateOfCreation;
        this.dateOfCompletion = dateOfCompletion;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(Date dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
