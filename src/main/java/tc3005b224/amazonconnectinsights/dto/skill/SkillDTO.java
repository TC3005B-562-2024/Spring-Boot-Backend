package tc3005b224.amazonconnectinsights.dto.skill;
import java.util.List;

import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;

public class SkillDTO {
    private int skill_id;
    private String title;
    private String description;
    private List<CriticalAlertDTO> critic_alerts;
    private List<AgentDTO> agents;

    // Constructor, getters y setters
    // Constructor vacío
    public SkillDTO() {}

    // Getters y setters
    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CriticalAlertDTO> getCritic_alerts() {
        return critic_alerts;
    }

    public void setCritic_alerts(List<CriticalAlertDTO> critic_alerts) {
        this.critic_alerts = critic_alerts;
    }

    public List<AgentDTO> getAgents() {
        return agents;
    }

    public void setAgents(List<AgentDTO> agents) {
        this.agents = agents;
    }
}
