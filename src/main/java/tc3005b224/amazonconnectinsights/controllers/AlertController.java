package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{alert_id}")
    public ResponseEntity<Optional<AlertDTO>> getAlert(@PathVariable("alert_id") String alert_id){
        List<AlertDTO> alerts = Arrays.asList(
                new AlertDTO("1", "El cliente se desconecto", "critica", "agent1", "skill1", "queue1", "contact1"),
                new AlertDTO("2", "Sistema en riesgo de sobrecarga", "alta", "agent2", "skill2", "queue2", "contact2"),
                new AlertDTO("3", "Error de conexión", "critica", "agent3", "skill3", "queue3", "contact3")
        );

        Optional<AlertDTO> criticalAlerts = alerts.stream()
                .filter(alert -> alert.getId().equals(alert_id))
                .findFirst();

        return ResponseEntity.ok(criticalAlerts);
    }
}
