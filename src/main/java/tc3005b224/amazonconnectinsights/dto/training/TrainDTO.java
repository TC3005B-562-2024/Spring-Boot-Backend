package tc3005b224.amazonconnectinsights.dto.training;

public class TrainDTO {
    private String agent_id;
    private boolean training;
    private String emotion_detected;
    private String description;
    public TrainDTO() {
    }
    

    public TrainDTO(String agent_id, boolean training, String emotion_detected, String description) {
        this.agent_id = agent_id;
        this.training = training;
        this.emotion_detected = emotion_detected;
        this.description = description;
    }

    // Getters and setters
    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(
        String agent_id) {
        this.agent_id = agent_id;
    }
    public boolean isTraining() {
        return training;
    }
    public void setIs_training(boolean
        training) {
        this.training = training;
    }
    public String getEmotion_detected() {
        return emotion_detected;
    }
    public void setEmotion_detected(String emotion_detected) {
        this.emotion_detected = emotion_detected;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}
