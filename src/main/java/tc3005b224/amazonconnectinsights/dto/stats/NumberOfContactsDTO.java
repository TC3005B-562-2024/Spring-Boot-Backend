package tc3005b224.amazonconnectinsights.dto.stats;

import java.util.Date;

public class NumberOfContactsDTO {
    private int numberOfContacts;
    private TimeWindowDTO timeWindow;

    public int getNumberOfContacts() {
        return numberOfContacts;
    }

    public void setNumberOfContacts(int numberOfContacts) {
        this.numberOfContacts = numberOfContacts;
    }

    public TimeWindowDTO getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindowDTO timeWindow) {
        this.timeWindow = timeWindow;
    }
}
