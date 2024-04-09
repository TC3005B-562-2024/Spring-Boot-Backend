package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.intervene.interveneDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/intervene")
public class interveneController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the timeline", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = interveneDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Timeline not found", content = @Content),
    })
    @Operation(summary = "Get all posts for a user")
    @PostMapping("/")
    public ResponseEntity<List<interveneDTO>> PostPosts() {
        List<interveneDTO> result = new ArrayList<>();
        interveneDTO post1 = new interveneDTO();
        post1.setAgent_id("123"); // Sample agent id
        result.add(post1);
        return ResponseEntity.ok(result);
    }

}
