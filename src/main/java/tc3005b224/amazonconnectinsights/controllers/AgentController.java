package tc3005b224.amazonconnectinsights.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.service.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAllAgents() {
        try {
            return ResponseEntity.ok(agentService.findByInstance("1", 10, "x"));
        }
        catch(Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{agentId}")
    public ResponseEntity<AgentDTO> getIndividualAgent() {
        try {
            return ResponseEntity.ok(agentService.findById("1", "1"));
        }
        catch(Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
