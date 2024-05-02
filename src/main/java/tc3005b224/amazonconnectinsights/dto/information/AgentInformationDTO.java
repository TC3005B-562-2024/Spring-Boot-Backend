package tc3005b224.amazonconnectinsights.dto.information;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgentInformationDTO {
    // -- ATTRIBUTES
    private String title;
    private List<InformationSectionDTO> sections = new ArrayList<>();

    // -- CONSTRUCTORS
    public AgentInformationDTO(String name, String skill, String status) {
        this.title = "Information";

        // Create a InformationSectionDTO for the agent's name.
        InformationSectionDTO nameSection = new InformationSectionDTO("Name", name, "black");

        // Create a InformationSectionDTO for the agent's skill.
        InformationSectionDTO skillSection = new InformationSectionDTO("Skill", skill, "black");

        // Create a InformationSectionDTO for the agent's skill.
        String statusColor = Objects.equals(status, "ROUTABLE") ? "green" : Objects.equals(status, "CUSTOM") ? "yellow" : "red";
        InformationSectionDTO statusSection = new InformationSectionDTO("Status", status, statusColor);

        // Add sections to the iterable
        this.sections.add(nameSection);
        this.sections.add(skillSection);
        this.sections.add(statusSection);
    }

    // -- GETTERS & SETTERS
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<InformationSectionDTO> getSections() {
        return sections;
    }

    public void setSections(List<InformationSectionDTO> sections) {
        this.sections = sections;
    }
}
