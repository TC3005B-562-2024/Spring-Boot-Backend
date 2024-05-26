package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tc3005b224.amazonconnectinsights.service.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @GetMapping
    public ResponseEntity<?> getAllAgents(
            @RequestParam(required = false, defaultValue = "") String resourceid) {
        try {
            return ResponseEntity.ok(agentService.findAll("1", resourceid));
        } catch (Exception e) {
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }

    @GetMapping("/{agentId}")
    public ResponseEntity<?> getIndividualAgent(@PathVariable String agentId) {
        try {
            return ResponseEntity.ok(agentService.findById("1", agentId));
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam(required = false) String routingProfileId) {
        try {
            return ResponseEntity.ok(agentService.findAvailableAgentNotInRoutingProfile("null", routingProfileId));
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }
}
