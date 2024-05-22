package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentDTO;
import tc3005b224.amazonconnectinsights.service.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @GetMapping
    public ResponseEntity<Iterable<AgentCardDTO>> getAllAgents(
            @RequestParam(required = false, defaultValue = "") String resourceid) {
        try {
            return ResponseEntity.ok(agentService.findAll("1", resourceid));
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{agentId}")
    public ResponseEntity<AgentDTO> getIndividualAgent(@PathVariable String agentId){
        try {
            return ResponseEntity.ok(agentService.findById("1", agentId));
        }
        catch(Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
