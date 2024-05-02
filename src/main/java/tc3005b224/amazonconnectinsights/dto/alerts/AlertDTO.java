package tc3005b224.amazonconnectinsights.dto.alerts;

import java.util.Date;

public class AlertDTO {
    private Short connectionId;

    private Short insightId;

    private Short trainingId;

    private String resource;

    private Boolean solved;

    private Boolean trainingCompleted;

    // Constructors, getters, and setters

    public AlertDTO(Short connectionId, Short insightId, Short trainingId, String resource, Boolean solved, Boolean trainingCompleted) {
        this.connectionId = connectionId;
        this.insightId = insightId;
        this.trainingId = trainingId;
        this.resource = resource;
        this.solved = solved;
        this.trainingCompleted = trainingCompleted;
    }

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

    public Short getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Short trainingId) {
        this.trainingId = trainingId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Boolean isSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Boolean getSolved() {
        return solved;
    }

    public Boolean getTrainingCompleted() {
        return trainingCompleted;
    }

    public void setTrainingCompleted(Boolean trainingCompleted) {
        this.trainingCompleted = trainingCompleted;
    }
}

