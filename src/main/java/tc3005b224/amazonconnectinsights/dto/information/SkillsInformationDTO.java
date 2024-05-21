package tc3005b224.amazonconnectinsights.dto.information;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SkillsInformationDTO {
    // -- ATTRIBUTES
    private String title;
    private List<InformationSectionDTO> sections = new ArrayList<>();
    private String alias;
    private Instant createdAt;
    private Long totalAgents;

    // -- CONSTRUCTORS
    public SkillsInformationDTO(String alias, Instant createdAt, Long totalAgents) {
        this.title = "Information";

        // Assign the new attributes
        this.alias = alias;
        this.createdAt = createdAt;
        this.totalAgents = totalAgents;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTotalAgents() {
        return totalAgents;
    }

    public void setTotalAgents(Long totalAgents) {
        this.totalAgents = totalAgents;
    }
}
