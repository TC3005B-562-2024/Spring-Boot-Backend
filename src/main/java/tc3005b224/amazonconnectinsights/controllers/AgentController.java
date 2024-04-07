package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.AgentAdherenceDTO;
import tc3005b224.amazonconnectinsights.dto.AgentAttendanceCallsDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/agent")
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

                AgentAdherenceDTO data = new AgentAdherenceDTO(id,
                                "{ \"start_time\": \"09:00\", \"end_time\": \"18:00\" }",
                                73.4);
                return ResponseEntity.ok(data);
        
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

}