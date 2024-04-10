package tc3005b224.amazonconnectinsights.controllers;
import java.util.ArrayList;
import java.util.List;

import tc3005b224.amazonconnectinsights.dto.training.TrainDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * TODO cambiar los datos de prueba
  
 */

@RestController
@RequestMapping("/train")

public class AgentTrainingController {

    @Autowired
    private TestData testData;
    @PostMapping("/add")
    public ResponseEntity<String> createTraining(@RequestBody TrainDTO trainDTO) {
        if (testData.getTrainingData().containsKey(trainDTO.getAgent_id())) {
            return ResponseEntity.badRequest().body("Training already exists for agent ID: " + trainDTO.getAgent_id());
        } else {
            testData.getTrainingData().put(trainDTO.getAgent_id(), trainDTO);
            return ResponseEntity.ok("Training created successfully");
        }
    }

    @PutMapping("/{agentId}")
    public ResponseEntity<String> updateTraining(@PathVariable String agentId, @RequestBody TrainDTO trainDTO) {
        if (!testData.getTrainingData().containsKey(agentId)) {
            return ResponseEntity.badRequest().body("Training not found for agent ID: " + agentId);
        }
        testData.getTrainingData().put(agentId, trainDTO);
        return ResponseEntity.ok("Training updated successfully");
    }


    @DeleteMapping("/{agentId}")
    public ResponseEntity<String> deleteTraining(@PathVariable String agentId) {
        if (!testData.getTrainingData().containsKey(agentId)) {
            return ResponseEntity.badRequest().body("Training not found for agent ID: " + agentId);
        }
        testData.getTrainingData().remove(agentId);
        return ResponseEntity.ok("Training deleted successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<TrainDTO>> getTrainings() {
        List<TrainDTO> trainings = new ArrayList<>(testData.getTrainingData().values());
        return ResponseEntity.ok(trainings);
    }
}

    