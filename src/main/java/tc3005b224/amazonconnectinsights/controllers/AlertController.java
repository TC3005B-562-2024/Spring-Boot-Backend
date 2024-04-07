package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert properly ignored.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action.", content = @Content),
    })
    @Operation(summary = "Ignore a desired alert.")
    @PostMapping("/{id}/ignore")
    public ResponseEntity<String> ignore(@PathVariable("id") int id){
        // TODO: Check if alert exists if not, return error 400.
        // TODO: If alert exists erase it logically.
        // TODO: Once the alert is erased, make it impossible for the system to generate the same type
        // of alert in a given contact-agent interaction.

        String result = String.format("Alert %d ignored.", id);
        return ResponseEntity.ok(result);
    }
}
