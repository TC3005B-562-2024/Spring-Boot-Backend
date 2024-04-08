package tc3005b224.amazonconnectinsights.dto;

import java.io.File;

public class TranscriptionDTO {
    private String content;
    private String id;
    private String participant_id;
    private String participant_role;
    private String time;
    private String display_name;
    private String sentiment;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(String participant_id) {
        this.participant_id = participant_id;
    }

    public String getParticipant_role() {
        return participant_role;
    }

    public void setParticipant_role(String participant_role) {
        this.participant_role = participant_role;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
