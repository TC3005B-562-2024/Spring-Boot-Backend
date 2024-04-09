package tc3005b224.amazonconnectinsights.dto.stats;

public class SpeedOfAnswerDTO {

    private double speedOfAnswerValue;
    private TimeWindowDTO timeWindow;

    public double getSpeedOfAnswerValue() {
        return speedOfAnswerValue;
    }

    public void setSpeedOfAnswerValue(double speedOfAnswerValue) {
        this.speedOfAnswerValue = speedOfAnswerValue;
    }

    public TimeWindowDTO getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindowDTO timeWindow) {
        this.timeWindow = timeWindow;
    }
}
