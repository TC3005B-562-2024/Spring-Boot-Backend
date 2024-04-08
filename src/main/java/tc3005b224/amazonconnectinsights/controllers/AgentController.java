package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tc3005b224.amazonconnectinsights.dto.skill.SkillDataDTO;
import tc3005b224.amazonconnectinsights.dto.AgentAdherenceDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentStateDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent properly transferred.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action.", content = @Content),
    })
    @Operation(summary = "Transfer an agent to a desired skill.")
    @PostMapping("/{id}/transfer")
    public ResponseEntity<String> transfer(@RequestBody SkillDataDTO skill, @PathVariable("id") int id)  {
        // TODO: Call API that shows status of the agent with the specified id.
        // TODO: Check status if on-call or disconnected send error.
        // TODO: Check status if available move to skill stablished.

        String result = String.format("Tranfer agent %d, to skill %s.", id, skill.getId());
        return ResponseEntity.ok(result);
    }
  
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent adherence found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentAdherenceDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent adherence not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Obtain adherence percentage of an agent")
    @GetMapping("/adherence")
    public ResponseEntity<AgentAdherenceDTO> getAdherenceData() {

        AgentAdherenceDTO data = new AgentAdherenceDTO(045, "{ \"start_time\": \"09:00\", \"end_time\": \"18:00\" }",
                73.4);
        return ResponseEntity.ok(data);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent Status found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentStateDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent Status not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Obtain status of an agent")
    @GetMapping("/status")
    public ResponseEntity<AgentStateDTO> getAgentStatus() {
        AgentStateDTO data = new AgentStateDTO("Available");
        return ResponseEntity.ok(data);
    }


}
