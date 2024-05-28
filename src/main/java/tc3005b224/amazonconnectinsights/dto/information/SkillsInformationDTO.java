package tc3005b224.amazonconnectinsights.dto.information;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SkillsInformationDTO {
    // -- ATTRIBUTES
    private String title;
    private List<InformationSectionDTO> sections = new ArrayList<>();

    // -- CONSTRUCTORS
    public SkillsInformationDTO(String alias, Instant createdAt, Long totalAgents) {
        this.title = "Skills Information";

        // Create a InformationSectionDTO for the agent's alias.
        InformationSectionDTO aliasSection = new InformationSectionDTO("Alias", alias, "black");

        // Create a InformationSectionDTO for the created at.
        InformationSectionDTO createdAtSection = new InformationSectionDTO("Created At", createdAt.toString(), "black");

        // Create a InformationSectionDTO for the total agents.
        InformationSectionDTO totalAgentsSection = new InformationSectionDTO("Total Agents", totalAgents.toString(), "black");

        // Add sections to the iterable
        this.sections.add(aliasSection);
        this.sections.add(createdAtSection);
        this.sections.add(totalAgentsSection);
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
