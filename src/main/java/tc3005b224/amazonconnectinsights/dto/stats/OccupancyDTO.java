package tc3005b224.amazonconnectinsights.dto.stats;

public class OccupancyDTO {

    private double occupancyValue;
    private TimeWindowDTO timeWindow;

    public double getOccupancyValue() {
        return occupancyValue;
    }

    public void setOccupancyValue(double occupancyValue) {
        this.occupancyValue = occupancyValue;
    }

    public TimeWindowDTO getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindowDTO timeWindow) {
        this.timeWindow = timeWindow;
    }
}