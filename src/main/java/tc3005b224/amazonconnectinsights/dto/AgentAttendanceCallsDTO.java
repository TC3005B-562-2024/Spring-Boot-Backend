package tc3005b224.amazonconnectinsights.dto;

public class AgentAttendanceCallsDTO {
    private int agentId;

    /*
     * Cantidad de llamadas atendidas por un agente
     */
    private int attendanceCalls;

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getAttendanceCalls() {
        return attendanceCalls;
    }

    public void setAttendanceCalls(int attendanceCalls) {
        this.attendanceCalls = attendanceCalls;
    }

    public AgentAttendanceCallsDTO(int agentId, int attendanceCalls) {
        this.agentId = agentId;
        this.attendanceCalls = attendanceCalls;
    }


}
