package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;

@RestController
@RequestMapping("/instance")
public class InstanceController {

    /**
     * Get the Amazon Connect instance data
     * 
     * TODO: Implement the method that retrieves the Amazon Connect instance data from the Amazon Connect API.
     * 
     * @return InstanceDTO
     * @author Diego Jacobo Djmr5
     * @see InstanceDTO
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found instance data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InstanceDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    @Operation(summary = "Get the Amazon Connect instance data")
    @GetMapping({ "/", "" })
    public ResponseEntity<InstanceDTO> getInstanceData() {

        InstanceDTO instance = new InstanceDTO(
                "ARN1234",
                "1",
                "Call Center CDMX",
                "https://amazonconnect.com/1",
                "ACTIVE",
                "Soporte",
                false,
                false,
                false);

        return ResponseEntity.ok(instance);
    }

}
