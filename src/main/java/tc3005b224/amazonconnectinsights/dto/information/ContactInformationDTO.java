package tc3005b224.amazonconnectinsights.dto.information;

import java.util.ArrayList;
import java.util.List;

public class ContactInformationDTO {
    // -- ATTRIBUTES
    private String title;
    private List<InformationSectionDTO> sections = new ArrayList<>();

    // -- CONSTRUCTORS
    public ContactInformationDTO(String contactId, String duration, Boolean durationAboveAverage, String emotion) {
        this.title = "Contact Information";

        // Create a InformationSectionDTO for the contacts's id.
        InformationSectionDTO nameSection = new InformationSectionDTO("Id", contactId, "black");
        this.sections.add(nameSection);

        // Create a InformationSectionDTO for the contacts's duration.
        String durationColor = durationAboveAverage ? "green" : "red";
        InformationSectionDTO skillSection = new InformationSectionDTO("Duration", duration, durationColor);
        this.sections.add(skillSection);

        // Create a InformationSectionDTO for the agent's skill.
        if (emotion != null) {
            String emotionColor = emotion.equals("POSITIVE") ? "green" : emotion.equals("NEUTRAL") ? "black" : "red";
            InformationSectionDTO statusSection = new InformationSectionDTO("Emotion", emotion, emotionColor);
            this.sections.add(statusSection);
        }
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
