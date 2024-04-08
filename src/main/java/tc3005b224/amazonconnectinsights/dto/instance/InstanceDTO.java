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
    private boolean contactLensEnabled;
    private boolean inboundCallsEnable;
    private boolean outboundCallsEnable;

    public InstanceDTO() {
    }

    public InstanceDTO(String arn, String id, String instanceAlias, String instanceAccessUrl, String instanceStatus,
            String serviceRole, boolean contactLensEnabled, boolean inboundCallsEnable, boolean outboundCallsEnable) {
        this.arn = arn;
        this.id = id;
        this.instanceAlias = instanceAlias;
        this.instanceAccessUrl = instanceAccessUrl;
        this.instanceStatus = instanceStatus;
        this.serviceRole = serviceRole;
        this.contactLensEnabled = contactLensEnabled;
        this.inboundCallsEnable = inboundCallsEnable;
        this.outboundCallsEnable = outboundCallsEnable;
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

    public boolean isContactLensEnabled() {
        return contactLensEnabled;
    }

    public void setContactLensEnabled(boolean contactLensEnabled) {
        this.contactLensEnabled = contactLensEnabled;
    }

    public boolean isInboundCallsEnable() {
        return inboundCallsEnable;
    }

    public void setInboundCallsEnable(boolean inboundCallsEnable) {
        this.inboundCallsEnable = inboundCallsEnable;
    }

    public boolean isOutboundCallsEnable() {
        return outboundCallsEnable;
    }

    public void setOutboundCallsEnable(boolean outboundCallsEnable) {
        this.outboundCallsEnable = outboundCallsEnable;
    }

}