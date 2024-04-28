package tc3005b224.amazonconnectinsights.dto.alerts;

import java.util.Date;

public class AlertDTO {
    private Short connectionId;

    private Short insightId;

    private Long trainingId;

    private String resource;

    private Date dateRegistered;

    private Date dateUpdated;

    private Boolean solved;

    private Date dateTrainingCompleted;

    // Constructors, getters, and setters

    public Short getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Short connectionId) {
        this.connectionId = connectionId;
    }

    public Short getInsightId() {
        return insightId;
    }

    public void setInsightId(Short insightId) {
        this.insightId = insightId;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    public Boolean isSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Date getDateTrainingCompleted() {
        return dateTrainingCompleted;
    }

    public void setDateTrainingCompleted(Date dateTrainingCompleted) {
        this.dateTrainingCompleted = dateTrainingCompleted;
    }
}

