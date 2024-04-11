package tc3005b224.amazonconnectinsights.dto.skill;

public class ScheduleDTO {
    private String day;
    private String start_time;
    private String end_time;

    // Constructor, getters y setters
    // Constructor vacío
    public ScheduleDTO() {}

    // Getters y setters
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}