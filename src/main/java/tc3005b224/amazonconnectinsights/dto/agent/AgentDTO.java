package tc3005b224.amazonconnectinsights.dto.agent;

import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.information.AgentInformationDTO;
import tc3005b224.amazonconnectinsights.dto.information.ContactInformationDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillBriefDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingDTO;

public class AgentDTO {
    // -- ATTRIBUTES
    private String id;
    private String resource;
    private Iterable<SkillBriefDTO> skills;
    private AgentInformationDTO agentInformationDTO;
    private ContactInformationDTO contactInformationDTO;
    private AlertPriorityDTO alertPriorityDTO;
    private Iterable<TrainingDTO> trainings;

    // -- CONSTRUCTOR

    public AgentDTO(String id, String resource, Iterable<SkillBriefDTO> skills, AgentInformationDTO agentInformationDTO, ContactInformationDTO contactInformationDTO, AlertPriorityDTO alertPriorityDTO, Iterable<TrainingDTO> trainings) {
        this.id = id;
        this.resource = resource;
        this.skills = skills;
        this.agentInformationDTO = agentInformationDTO;
        this.contactInformationDTO = contactInformationDTO;
        this.alertPriorityDTO = alertPriorityDTO;
        this.trainings = trainings;
    }

    // -- GETTERS & SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Iterable<SkillBriefDTO> getSkills() {
        return skills;
    }

    public void setSkills(Iterable<SkillBriefDTO> skills) {
        this.skills = skills;
    }

    public AgentInformationDTO getAgentInformationDTO() {
        return agentInformationDTO;
    }

    public void setAgentInformationDTO(AgentInformationDTO agentInformationDTO) {
        this.agentInformationDTO = agentInformationDTO;
    }

    public ContactInformationDTO getContactInformationDTO() {
        return contactInformationDTO;
    }

    public void setContactInformationDTO(ContactInformationDTO contactInformationDTO) {
        this.contactInformationDTO = contactInformationDTO;
    }

    public AlertPriorityDTO getAlertPriorityDTO() {
        return alertPriorityDTO;
    }

    public void setAlertPriorityDTO(AlertPriorityDTO alertPriorityDTO) {
        this.alertPriorityDTO = alertPriorityDTO;
    }

    public Iterable<TrainingDTO> getTrainings() {
        return trainings;
    }

    public void setTrainings(Iterable<TrainingDTO> trainings) {
        this.trainings = trainings;
    }
}
