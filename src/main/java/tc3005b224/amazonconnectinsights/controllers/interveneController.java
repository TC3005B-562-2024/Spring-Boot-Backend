package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.intervene.interveneDTO;

@RestController
@RequestMapping("/intervene")
public class interveneController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Intervention in progress", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = interveneDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Agent not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Intervene in a contact")
    @PostMapping("/")
    public ResponseEntity<interveneDTO> PostPosts(@RequestBody interveneDTO intervene) {
        return ResponseEntity.ok(intervene);
    }

}
