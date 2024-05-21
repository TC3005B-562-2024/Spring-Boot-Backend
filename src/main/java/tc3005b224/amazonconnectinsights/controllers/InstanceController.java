package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;
import tc3005b224.amazonconnectinsights.service.InstanceService;

@RestController
@RequestMapping("/instance")
public class InstanceController {
    @Autowired
    private InstanceService instanceService;

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
            @ApiResponse(responseCode = "200", description = "Found Instance Data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InstanceDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })

    // CONNECTION WITH AWS API ENDPOINT TO RETURN INSTANCE DETAILS
    @Operation(summary = "Get the Amazon Connect Instance Details")
    @GetMapping
    public ResponseEntity<InstanceDTO> getInstanceData(@RequestParam(required = true) String token) {
        try {
            InstanceDTO response = instanceService.getInstanceDetails(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
    // CONNECTION WITH AWS API ENDPOINT TEST TO RETURN CONNECITON ERROR
    @Operation(summary = "Test Endpoint For Connection Error With Amazon Connect API")
    @GetMapping("/test")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Amazon Connect API", content = @Content),
    })
    public ResponseEntity<InstanceDTO> getInstanceDataError(@RequestParam(required = true) String token) {
        try {
            InstanceDTO response = instanceService.getInstanceDetailsWithError(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
