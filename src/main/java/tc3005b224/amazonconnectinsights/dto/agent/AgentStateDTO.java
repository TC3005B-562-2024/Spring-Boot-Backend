package tc3005b224.amazonconnectinsights.dto.agent;

public class AgentStateDTO {
    // -- ATTRIBUTES
    private String state;


    // -- METHODS
    // Constructor Method
    public AgentStateDTO(String state) {
        this.state = state;
    }

    // Getters and Setters
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
