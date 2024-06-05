package tc3005b224.amazonconnectinsights.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.instance.InstanceDTO;
import tc3005b224.amazonconnectinsights.service.InstanceService;

@RestController
@RequestMapping("/instance")
public class InstanceController {
    @Autowired
    private InstanceService instanceService;

    @Operation(
        summary = "Get the Amazon Connect Instance Details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Found Instance Data",
                content = {
                    @Content(
                        mediaType = "application/json", 
                        schema = @Schema(implementation = InstanceDTO.class)
                    )
                }
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Invalid alertIdentifier."
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error", content = @Content
            ),
            @ApiResponse(
                responseCode = "503", 
                description = "Couldn't connect to Amazon Connect API", content = @Content
            ),
        }
    )

    @GetMapping
    public ResponseEntity<?> getInstanceData(Principal principal) {
        try {
            InstanceDTO response = instanceService.getInstanceDetails(principal.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }
}
