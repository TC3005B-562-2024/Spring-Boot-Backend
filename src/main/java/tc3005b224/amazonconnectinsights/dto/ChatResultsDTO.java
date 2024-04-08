package tc3005b224.amazonconnectinsights.dto;

public class ChatResultsDTO {
    private Integer chat_id;
    private Integer agent_id;
    private TranscriptionDTO results;
    private String status;

    public Integer getChat_id() {
        return chat_id;
    }

    public void setChat_id(Integer chat_id) {
        this.chat_id = chat_id;
    }
    public Integer getAgent_id() {
        return agent_id;
    }
    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }
    public TranscriptionDTO getResults() {
        return results;
    }
    public void setResults(TranscriptionDTO results) {
        this.results = results;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
