package tc3005b224.amazonconnectinsights.dto.agent;

public class AgentAcrDTO {
    // -- ATTRIBUTES
    private int agentId;
    private int incomingCalls;
    private int unansweredCalls;

    // -- METHODS
    // Constructor Method
    public AgentAcrDTO(int agentId, int incomingCalls, int unansweredCalls) {
        this.agentId = agentId;
        this.incomingCalls = incomingCalls;
        this.unansweredCalls = unansweredCalls;
    }

    // Getters and Setters


    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getIncomingCalls() {
        return incomingCalls;
    }

    public void setIncomingCalls(int incomingCalls) {
        this.incomingCalls = incomingCalls;
    }

    public int getUnansweredCalls() {
        return unansweredCalls;
    }

    public void setUnansweredCalls(int unansweredCalls) {
        this.unansweredCalls = unansweredCalls;
    }
}
