package tc3005b224.amazonconnectinsights.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.agent.AgentAvailableToTransferListDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentCardDTO;
import tc3005b224.amazonconnectinsights.service.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @Operation(summary = "Returns a list of agents that might be filtered by a resource id.", responses = {
            @ApiResponse(responseCode = "200", description = "List of agents.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AgentCardDTO.class)))

            }),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect."),
    })
    @GetMapping
    public ResponseEntity<?> getAllAgents(
            @RequestParam(required = false, defaultValue = "") String resourceid, Principal principal) {
        try {
            return ResponseEntity.ok(agentService.findAll(principal.getName(), resourceid));
        } catch (Exception e) {
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }

    @Operation(summary = "Returns an agent by its id.", responses = {
            @ApiResponse(responseCode = "200", description = "Agent data found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentCardDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent not found."),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect."),
    })
    @GetMapping("/{agentId}")
    public ResponseEntity<?> getIndividualAgent(@PathVariable String agentId, Principal principal) {
        try {
            return ResponseEntity.ok(agentService.findById(principal.getName(), agentId));
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }

    @Operation(summary = "Returns a list of agents that are available to transfer.", responses = {
            @ApiResponse(responseCode = "200", description = "List of agents available to transfer.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentAvailableToTransferListDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect."),
    })
    @GetMapping("/available-to-transfer")
    public ResponseEntity<?> test(@RequestParam(required = true) String routingProfileId, Principal principal) {
        try {
            return ResponseEntity.ok(agentService.findAvailableAgentNotInRoutingProfile(principal.getName(), routingProfileId));
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }

    @GetMapping("/{agentId}/contacts")
    public ResponseEntity<?> getAgentContacts(@PathVariable String agentId, Principal principal) {
        try {
            return ResponseEntity.ok(agentService.getNegativeSentimentContacts(principal.getName(), agentId));
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }
}
