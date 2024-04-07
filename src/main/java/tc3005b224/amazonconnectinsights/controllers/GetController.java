package tc3005b224.amazonconnectinsights.controllers;


import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.AgentDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/get")
public class GetController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the agents", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AgentDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agents not found", content = @Content),
    })
    @Operation(summary = "Get the states of all agents")
    @GetMapping("/agents")
    public ResponseEntity<List<AgentDTO>> getAgents() {
        List<AgentDTO> result = new ArrayList<>();
        AgentDTO agent1 = new AgentDTO();
        agent1.setState("Available");
        result.add(agent1);
        return ResponseEntity.ok(result);
    }

    //TODO Implementar el endpoint /get/agent/{id} que regrese un solo agente con el id solicitado


}
