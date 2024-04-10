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

package tc3005b224.amazonconnectinsights.dto.stats;

import java.util.Date;

public class TimeWindowDTO {
    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
