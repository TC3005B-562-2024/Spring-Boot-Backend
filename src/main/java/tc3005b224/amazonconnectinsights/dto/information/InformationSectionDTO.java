package tc3005b224.amazonconnectinsights.dto.information;

public class InformationSectionDTO {
    // -- ATTRIBUTES
    private String sectionTitle;
    private String sectionValue;
    private String color;

    // -- CONSTRUCTOR
    public InformationSectionDTO(String sectionTitle, String sectionValue, String color) {
        this.sectionTitle = sectionTitle;
        this.sectionValue = sectionValue;
        this.color = color;
    }

    // -- GETTERS & SETTERS
    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionValue() {
        return sectionValue;
    }

    public void setSectionValue(String sectionValue) {
        this.sectionValue = sectionValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
