package tc3005b224.amazonconnectinsights.dto.alerts;


import java.util.List;

import tc3005b224.amazonconnectinsights.models_sql.Alert;

public class AlertPriorityDTO {
    private List<Alert> high;
    private List<Alert> medium;
    private List<Alert> low;

    public List<Alert> getHigh() {
        return high;
    }

    public void setHigh(List<Alert> high) {
        this.high = high;
    }

    public List<Alert> getMedium() {
        return medium;
    }

    public void setMedium(List<Alert> medium) {
        this.medium = medium;
    }

    public List<Alert> getLow() {
        return low;
    }

    public void setLow(List<Alert> low) {
        this.low = low;
    }
}
