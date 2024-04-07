package tc3005b224.amazonconnectinsights.dto.agent;

public class AgentDTO {
    // -- ATTRIBUTES
    private String arn;
    private String id;
    private IdentityInfoDTO identityInfo;
    private String routingProfileId;
    private String username;
    private String agentContactState;
    private String agentPauseDurationInSeconds;
    private String connectedToAgentTimestamp;

    // -- METHODS
    // Constructor Method
    public AgentDTO(String arn, String id, IdentityInfoDTO identityInfo, String routingProfileId, String username, String agentContactState, String agentPauseDurationInSeconds, String connectedToAgentTimestamp) {
        this.arn = arn;
        this.id = id;
        this.identityInfo = identityInfo;
        this.routingProfileId = routingProfileId;
        this.username = username;
        this.agentContactState = agentContactState;
        this.agentPauseDurationInSeconds = agentPauseDurationInSeconds;
        this.connectedToAgentTimestamp = connectedToAgentTimestamp;
    }

    // Getters and Setters
    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdentityInfoDTO getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(IdentityInfoDTO identityInfo) {
        this.identityInfo = identityInfo;
    }

    public String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAgentContactState() {
        return agentContactState;
    }

    public void setAgentContactState(String agentContactState) {
        this.agentContactState = agentContactState;
    }

    public String getAgentPauseDurationInSeconds() {
        return agentPauseDurationInSeconds;
    }

    public void setAgentPauseDurationInSeconds(String agentPauseDurationInSeconds) {
        this.agentPauseDurationInSeconds = agentPauseDurationInSeconds;
    }

    public String getConnectedToAgentTimestamp() {
        return connectedToAgentTimestamp;
    }

    public void setConnectedToAgentTimestamp(String connectedToAgentTimestamp) {
        this.connectedToAgentTimestamp = connectedToAgentTimestamp;
    }
}
