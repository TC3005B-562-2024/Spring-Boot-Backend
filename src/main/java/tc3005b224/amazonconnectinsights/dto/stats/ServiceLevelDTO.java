package tc3005b224.amazonconnectinsights.dto.stats;

public class ServiceLevelDTO {
    private double serviceLevelValue;
    private TimeWindowDTO timeWindow;

    public double getServiceLevelValue() {
        return serviceLevelValue;
    }

    public void setServiceLevelValue(double serviceLevelValue) {
        this.serviceLevelValue = serviceLevelValue;
    }

    public TimeWindowDTO getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindowDTO timeWindow) {
        this.timeWindow = timeWindow;
    }
}
