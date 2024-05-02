package tc3005b224.amazonconnectinsights.dto.skill;

public class SkillBriefDTO {
    // -- ATTRIBUTES
    private String id;
    private String resource;
    private String alias;
    private String iconName;

    // -- CONSTRUCTORS
    public SkillBriefDTO(String id, String resource, String alias, String iconName) {
        this.id = id;
        this.resource = resource;
        this.alias = alias;
        this.iconName = iconName;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
