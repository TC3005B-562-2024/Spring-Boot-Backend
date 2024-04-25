package tc3005b224.amazonconnectinsights.dto.alerts;

import java.util.List;

public class ListAlertDTO {
    private List<AlertDTO> alerts;

    public ListAlertDTO(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }

    public List<AlertDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }
}
