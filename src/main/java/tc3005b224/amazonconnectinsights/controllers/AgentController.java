package tc3005b224.amazonconnectinsights.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.AgentAdherenceDTO;
import tc3005b224.amazonconnectinsights.dto.AgentAttendanceCallsDTO;
import tc3005b224.amazonconnectinsights.dto.EmotionDTO;
import tc3005b224.amazonconnectinsights.dto.InfoCallsDTO;
import tc3005b224.amazonconnectinsights.dto.agent.AgentStateDTO;
import tc3005b224.amazonconnectinsights.dto.skill.SkillDataDTO;

@RestController
@RequestMapping("/agents")
public class AgentController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent adherence found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentAdherenceDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent adherence not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Obtain adherence percentage of an agent")
    @GetMapping("{agentId}/adherence")
    public ResponseEntity<AgentAdherenceDTO> getAdherenceData(@PathVariable("agentId") int id) {
        // TODO: Create schedule DTO or model to return the schedule of the agent.
        AgentAdherenceDTO data = new AgentAdherenceDTO(id,
                "{ \"start_time\": \"09:00\", \"end_time\": \"18:00\" }",
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent properly transferred.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action.", content = @Content),
    })
    @Operation(summary = "Transfer an agent to a desired skill.")
    @PostMapping("/{id}/transfer")
    public ResponseEntity<String> transfer(@RequestBody SkillDataDTO skill, @PathVariable("id") int id) {
        // TODO: Call API that shows status of the agent with the specified id.
        // TODO: Check status if on-call or disconnected send error.
        // TODO: Check status if available move to skill stablished.

        String result = String.format("Tranfer agent %d, to skill %s.", id, skill.getId());
        return ResponseEntity.ok(result);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent attendance found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentAttendanceCallsDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent attendance not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Obtain the attended calls of an agent")
    @GetMapping("{agentId}/attendanceCalls")
    public ResponseEntity<AgentAttendanceCallsDTO> getAttendenceCallsData(@PathVariable("agentId") int id) {

        AgentAttendanceCallsDTO data = new AgentAttendanceCallsDTO(id, 1);
        return ResponseEntity.ok(data);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found call information", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InfoCallsDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent or Call ID not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server couldn't process the application", content = @Content),
            @ApiResponse(responseCode = "503", description = "Server couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Get the individual information of a call attended by an specific agent during the day")
    @GetMapping("/{agentId}/calls/{callId}")
    public ResponseEntity<List<InfoCallsDTO>> getCallInfo(@PathVariable("agentId") int agentId,
            @PathVariable("callId") int callId) {
        List<InfoCallsDTO> result = new ArrayList<>();
        InfoCallsDTO call1 = new InfoCallsDTO();
        call1.setAgent_id(agentId);
        call1.setClient_id(callId);
        call1.setDate("2024-04-07 15:30:00");
        call1.setCall_duration("01:30:15");
        List<EmotionDTO> emotionsdetected = new ArrayList<>();
        EmotionDTO e1 = new EmotionDTO();
        e1.setEmotion("angry");
        e1.setTime("00:50:00");
        emotionsdetected.add(e1);
        call1.setEmotions_detected(emotionsdetected);
        result.add(call1);
        return ResponseEntity.ok(result);
    }

}
