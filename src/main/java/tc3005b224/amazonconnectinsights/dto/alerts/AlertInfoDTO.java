package tc3005b224.amazonconnectinsights.dto.alerts;

/**
 * Data Transfer Object for alerts statistics information
 * 
 * @author Diego Jacobo Djmr5
 */
public class AlertInfoDTO {

    private int totalAlerts;
    private int criticalAlerts;
    private int mediumAlerts;
    private int lowAlerts;

    public AlertInfoDTO() {
    }

    public AlertInfoDTO(int totalAlerts, int criticalAlerts, int mediumAlerts, int lowAlerts) {
        this.totalAlerts = totalAlerts;
        this.criticalAlerts = criticalAlerts;
        this.mediumAlerts = mediumAlerts;
        this.lowAlerts = lowAlerts;
    }

    public int getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(int totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    public int getCriticalAlerts() {
        return criticalAlerts;
    }

    public void setCriticalAlerts(int criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    public int getMediumAlerts() {
        return mediumAlerts;
    }

    public void setMediumAlerts(int mediumAlerts) {
        this.mediumAlerts = mediumAlerts;
    }

    public int getLowAlerts() {
        return lowAlerts;
    }

    public void setLowAlerts(int lowAlerts) {
        this.lowAlerts = lowAlerts;
    }

}
