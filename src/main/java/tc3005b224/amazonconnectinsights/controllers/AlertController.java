package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.Alerts.AlertAllDTO;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert properly ignored.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action.", content = @Content),
    })

    @GetMapping("/all")
    public List<AlertAllDTO> getAllAlerts() {
        AlertAllDTO Alert = new AlertAllDTO();
        Alert.setId("1");
        Alert.setDescription("El cliente se enfurecio");
        Alert.setPriority("Critica");
        Alert.setAgentId("agent1");
        Alert.setSkillId("skill1");
        Alert.setQueueId("queue1");
        Alert.setContactId("contact1");

        AlertAllDTO Alert2 = new AlertAllDTO();
        Alert2.setId("2");
        Alert2.setDescription("El cliente se enfurecio mucho");
        Alert2.setPriority("Critica");
        Alert2.setAgentId("agent1");
        Alert2.setSkillId("skill1");
        Alert2.setQueueId("queue1");
        Alert2.setContactId("contact1");

        return Arrays.asList(Alert, Alert2);
    }

    @DeleteMapping("/{alert_id}")
    public ResponseEntity<String> deleteAlert(@PathVariable("alert_id") String alertId) {
        return ResponseEntity.ok("Alert " + alertId + " was deleted");
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
}
