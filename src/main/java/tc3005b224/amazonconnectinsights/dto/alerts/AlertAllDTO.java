package tc3005b224.amazonconnectinsights.dto.alerts;

import java.util.List;

public class AlertAllDTO {
    private List<AlertDTO> alerts;

    public AlertAllDTO() {
    }

    public AlertAllDTO(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }

    public List<AlertDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }

}