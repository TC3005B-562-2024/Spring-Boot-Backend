package tc3005b224.amazonconnectinsights.dto.instance;

/**
 * Data Transfer Object for Amazon Connect Instance entity information
 * 
 * @author Diego Jacobo Djmr5
 */
public class InstanceDTO {

    /**
     * Instance ARN - Amazon Resource Name (ARN) is a unique global identifier that
     * identifies a resource
     */
    private String arn;
    /**
     * Instance ID - Unique identifier inside the Amazon Connect instance
     */
    private String id;
    private String instanceAlias;
    private String instanceAccessUrl;
    private String instanceStatus;
    private String serviceRole;
    /**
     * Indicates if Contact Lens is enabled in the instance (Sentiment analysis)
     */
    private String contactLensEnabled;
    private String inboundCallsEnable;
    private String outboundCallsEnable;

    public InstanceDTO() {
    }
    public InstanceDTO(String arn, String id, String outboundCallsEnable, String instanceAlias, String instanceAccessUrl, String instanceStatus, String serviceRole, String contactLensEnabled, String inboundCallsEnable) {
        this.arn = arn;
        this.id = id;
        this.outboundCallsEnable = outboundCallsEnable;
        this.instanceAlias = instanceAlias;
        this.instanceAccessUrl = instanceAccessUrl;
        this.instanceStatus = instanceStatus;
        this.serviceRole = serviceRole;
        this.contactLensEnabled = contactLensEnabled;
        this.inboundCallsEnable = inboundCallsEnable;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceAlias() {
        return instanceAlias;
    }

    public void setInstanceAlias(String instanceAlias) {
        this.instanceAlias = instanceAlias;
    }

    public String getInstanceAccessUrl() {
        return instanceAccessUrl;
    }

    public void setInstanceAccessUrl(String instanceAccessUrl) {
        this.instanceAccessUrl = instanceAccessUrl;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public String getServiceRole() {
        return serviceRole;
    }

    public void setServiceRole(String serviceRole) {
        this.serviceRole = serviceRole;
    }

    public String getContactLensEnabled() {
        return contactLensEnabled;
    }

    public void setContactLensEnabled(String contactLensEnabled) {
        this.contactLensEnabled = contactLensEnabled;
    }

    public String getInboundCallsEnable() {
        return inboundCallsEnable;
    }

    public void setInboundCallsEnable(String inboundCallsEnable) {
        this.inboundCallsEnable = inboundCallsEnable;
    }

    public String getOutboundCallsEnable() {
        return outboundCallsEnable;
    }

    public void setOutboundCallsEnable(String outboundCallsEnable) {
        this.outboundCallsEnable = outboundCallsEnable;
    }
}
