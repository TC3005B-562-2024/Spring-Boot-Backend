package tc3005b224.amazonconnectinsights.dto;

 
/**Data Transfer Object for Amazon Connect Instance entity information
*@author Ian Sanchez elyansan
*/

public class AgentAdherenceDTO {
    private int agentId;

    /**
     * Horario del agente
     * TODO Crear DTO de Schedule
     */
    private String schedule;

    /**
     * Porcentaje de adherencia
     */
    private double effectiveTime;

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public double getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(double effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public AgentAdherenceDTO(int agentId, String schedule, double effectiveTime) {
        this.agentId = agentId;
        this.schedule = schedule;
        this.effectiveTime = effectiveTime;
    }

}
