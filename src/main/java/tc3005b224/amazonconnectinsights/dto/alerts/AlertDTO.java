package tc3005b224.amazonconnectinsights.dto.alerts;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

import java.util.Date;

public class AlertDTO {
    private long identifier;
    private String resource;
    private Date dateRegistered;
    private Date dateUpdated;
    private int priority;
    private boolean isSolved;
    private Date dateTrainingCompleted;

    // Constructor
    public AlertDTO(Alert alert) {
        this.identifier = alert.getIdentifier();
        this.resource = alert.getResourceArn();
        this.dateRegistered = alert.getDateRegistered();
        this.dateUpdated = alert.getDateUpdated();
        this.isSolved = alert.getSolved();
        this.dateTrainingCompleted = alert.getDateTrainingCompleted();
    }

    public AlertDTO(long identifier, String resource, Date dateRegistered, Date dateUpdated, int priority, boolean isSolved, Date dateTrainingCompleted) {
        this.identifier = identifier;
        this.resource = resource;
        this.dateRegistered = dateRegistered;
        this.dateUpdated = dateUpdated;
        this.priority = priority;
        this.isSolved = isSolved;
        this.dateTrainingCompleted = dateTrainingCompleted;
    }

    // Getters & Setters
    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "AlertDTO{" +
                "priority=" + priority +
                '}';
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public Date getDateTrainingCompleted() {
        return dateTrainingCompleted;
    }

    public void setDateTrainingCompleted(Date dateTrainingCompleted) {
        this.dateTrainingCompleted = dateTrainingCompleted;
    }
}
