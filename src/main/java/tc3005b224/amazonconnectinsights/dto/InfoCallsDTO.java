package tc3005b224.amazonconnectinsights.dto;

import java.util.List;

public class InfoCallsDTO {
    private Integer agent_id;
    private Integer client_id;
    private String date;
    private String call_duration;
    private List<EmotionDTO> emotions_detected;

    public Integer getAgent_id() {
        return agent_id;
    }
    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }
    public Integer getClient_id() {
        return client_id;
    }
    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCall_duration() {
        return call_duration;
    }
    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }
    public List<EmotionDTO> getEmotions_detected() {
        return emotions_detected;
    }
    public void setEmotions_detected(List<EmotionDTO> emotions_detected) {
        this.emotions_detected = emotions_detected;
    }


}

