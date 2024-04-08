package tc3005b224.amazonconnectinsights.dto.Alerts;

public class AlertDTO {
    private String id;
    private  String description;
    private String priority;
    private String agentId;
    private String skillId;
    private String queueId;
    private String contactId;

    public AlertDTO() {}

    public AlertDTO(String id, String description, String priority, String agentId, String skillId, String queueId, String contactId) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.agentId = agentId;
        this.skillId = skillId;
        this.queueId = queueId;
        this.contactId = contactId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
