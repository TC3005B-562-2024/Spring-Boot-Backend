package tc3005b224.amazonconnectinsights.dto.alerts;

public class AlertDTO {
    private Short connectionId;

    private Short insightId;

    private Short trainingId;

    private String interveneContact;

    private String interveneAgent;

    private String originalRoutingProfile;

    private String destinationRoutingProfile;

    private String transferedAgent;

    private String resource;

    private Boolean solved;

    private Boolean trainingCompleted;

    // Constructors, getters, and setters
    public AlertDTO() {
        ;
    }
    
    public AlertDTO(Short connectionId, Short insightId, Short trainingId, String interveneContact, String interveneAgent, String originalRoutingProfile, String destinationRoutingProfile, String transferedAgent, String resource, Boolean solved, Boolean trainingCompleted) {
        this.connectionId = connectionId;
        this.insightId = insightId;
        this.trainingId = trainingId;
        this.interveneContact = interveneContact;
        this.interveneAgent = interveneAgent;
        this.originalRoutingProfile = originalRoutingProfile;
        this.destinationRoutingProfile = destinationRoutingProfile;
        this.transferedAgent = transferedAgent;
        this.resource = resource;
        this.solved = solved;
        this.trainingCompleted = trainingCompleted;
    }

    public static AlertDTO newTrainingAlertDTO(Short connectionId, Short insightId, Short trainingId, String resource) {
        return new AlertDTO(connectionId, insightId, trainingId, null, null, null, null, null, resource, null, null);
    }

    public static AlertDTO newInterventionAlertDTO(Short connectionId, Short insightId, String interveneContact, String interveneAgent, String resource) {
        return new AlertDTO(connectionId, insightId, null, interveneContact, interveneAgent, null, null, null, resource, null, null);
    }

    public static AlertDTO newTransferAlertDTO(Short connectionId, Short insightId, String originalRoutingProfile, String destinationRoutingProfile, String transferedAgent, String resource) {
        return new AlertDTO(connectionId, insightId, null, null, null, originalRoutingProfile, destinationRoutingProfile, transferedAgent, resource, null, null);
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

    public String getInterveneContact() {
        return interveneContact;
    }

    public void setInterveneContact(String interveneContact) {
        this.interveneContact = interveneContact;
    }

    public String getInterveneAgent() {
        return interveneAgent;
    }

    public void setInterveneAgent(String interveneAgent) {
        this.interveneAgent = interveneAgent;
    }

    public String getOriginalRoutingProfile() {
        return originalRoutingProfile;
    }

    public void setOriginalRoutingProfile(String originalRoutingProfile) {
        this.originalRoutingProfile = originalRoutingProfile;
    }

    public String getDestinationRoutingProfile() {
        return destinationRoutingProfile;
    }

    public void setDestinationRoutingProfile(String destinationRoutingProfile) {
        this.destinationRoutingProfile = destinationRoutingProfile;
    }

    public String getTransferedAgent() {
        return transferedAgent;
    }

    public void setTransferedAgent(String transferedAgent) {
        this.transferedAgent = transferedAgent;
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
