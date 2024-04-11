package tc3005b224.amazonconnectinsights.dto.skill;

import java.time.LocalDateTime;

public class CriticalAlertDTO {
    private int agent_id;
    private LocalDateTime hora_y_fecha_inicio_llamada;
    private String insight;
    private String prioridad;

    // Constructor, getters y setters
    // Constructor vacío
    public CriticalAlertDTO() {}

    // Getters y setters
    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public LocalDateTime getHora_y_fecha_inicio_llamada() {
        return hora_y_fecha_inicio_llamada;
    }

    public void setHora_y_fecha_inicio_llamada(LocalDateTime hora_y_fecha_inicio_llamada) {
        this.hora_y_fecha_inicio_llamada = hora_y_fecha_inicio_llamada;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
