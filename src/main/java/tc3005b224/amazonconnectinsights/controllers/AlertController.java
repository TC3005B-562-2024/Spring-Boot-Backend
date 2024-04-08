package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tc3005b224.amazonconnectinsights.dto.Alerts.AlertDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/alerts")
public class AlertController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert properly ignored.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action.", content = @Content),
    })

    @GetMapping("/all")
    public ResponseEntity<List<AlertDTO>> getAllAlerts() {
        AlertDTO Alert = new AlertDTO();
        Alert.setId("1");
        Alert.setDescription("El cliente se enfurecio");
        Alert.setPriority("Critica");
        Alert.setAgentId("agent1");
        Alert.setSkillId("skill1");
        Alert.setQueueId("queue1");
        Alert.setContactId("contact1");

        AlertDTO Alert2 = new AlertDTO();
        Alert2.setId("2");
        Alert2.setDescription("El cliente se enfurecio mucho");
        Alert2.setPriority("Critica");
        Alert2.setAgentId("agent1");
        Alert2.setSkillId("skill1");
        Alert2.setQueueId("queue1");
        Alert2.setContactId("contact1");

        List<AlertDTO> alerts = Arrays.asList(Alert, Alert2);

        return ResponseEntity.ok(alerts);
    }

    @DeleteMapping("/{alert_id}")
    public ResponseEntity<String> deleteAlert(@PathVariable("alert_id") String alertId) {
        return ResponseEntity.ok("Alert " + alertId + " was deleted");
    }

    @GetMapping("/critics")
    public ResponseEntity<List<AlertDTO>> getCriticalAlerts() {
        List<AlertDTO> alerts = Arrays.asList(
                new AlertDTO("1", "El cliente se suicidó", "critica", "agent1", "skill1", "queue1", "contact1"),
                new AlertDTO("2", "Sistema en riesgo de sobrecarga", "alta", "agent2", "skill2", "queue2", "contact2"),
                new AlertDTO("3", "Error de conexión", "critica", "agent3", "skill3", "queue3", "contact3")
        );

        List<AlertDTO> criticalAlerts = alerts.stream()
                .filter(alert -> "critica".equals(alert.getPriority()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(criticalAlerts);
    }
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
    @PutMapping("/{alert_id}")
    public ResponseEntity<String> putAlert(@PathVariable("alert_id") String alertId) {
        return ResponseEntity.ok("Alert " + alertId + " was updated");
    }
}
