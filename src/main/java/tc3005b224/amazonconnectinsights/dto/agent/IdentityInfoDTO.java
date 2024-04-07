package tc3005b224.amazonconnectinsights.dto.agent;

public class IdentityInfoDTO {
    // -- ATTRIBUTES
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String secondaryEmail;

    // -- METHODS
    // Constructor Method
    public IdentityInfoDTO(String email, String firstName, String lastName, String mobile, String secondaryEmail) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.secondaryEmail = secondaryEmail;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }
}
