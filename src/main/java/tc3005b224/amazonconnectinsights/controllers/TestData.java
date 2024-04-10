package tc3005b224.amazonconnectinsights.controllers;
import tc3005b224.amazonconnectinsights.dto.training.TrainDTO;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class TestData {
    private Map<String, TrainDTO> trainingData = new HashMap<>();

    public TestData() {
        // Agregar datos de prueba
        TrainDTO training1 = new TrainDTO("123456", true, "Happy", "This training session was very informative.");
        TrainDTO training2 = new TrainDTO("789012", true, "Sad", "I didn't learn much in this training.");
        trainingData.put("123456", training1);
        trainingData.put("789012", training2);
    }

    public Map<String, TrainDTO> getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(Map<String, TrainDTO> trainingData) {
        this.trainingData = trainingData;
    }
}
