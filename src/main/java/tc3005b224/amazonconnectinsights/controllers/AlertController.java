package tc3005b224.amazonconnectinsights.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertAllDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertInfoDTO;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts Found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlertAllDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
    })
    @Operation(summary = "Get all alerts.")
    @GetMapping("/all")
    public ResponseEntity<AlertAllDTO> getAllAlerts() {
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

        AlertAllDTO alerts = new AlertAllDTO();
        alerts.setAlerts(Arrays.asList(Alert, Alert2));

        return ResponseEntity.ok(alerts);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert properly deleted.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Alert not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
    })
    @Operation(summary = "Delete an alert.")
    @DeleteMapping("/{alert_id}")
    public ResponseEntity<String> deleteAlert(@PathVariable("alert_id") String alertId) {
        return ResponseEntity.ok("Alert " + alertId + " was deleted");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Critical alerts found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlertAllDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
    })
    @Operation(summary = "Get all critical alerts.")
    @GetMapping("/critics")
    public ResponseEntity<AlertAllDTO> getCriticalAlerts() {
        List<AlertDTO> alerts = Arrays.asList(
                new AlertDTO("1", "El cliente se suicidó", "critica", "agent1", "skill1", "queue1", "contact1"),
                new AlertDTO("2", "Sistema en riesgo de sobrecarga", "alta", "agent2", "skill2", "queue2", "contact2"),
                new AlertDTO("3", "Error de conexión", "critica", "agent3", "skill3", "queue3", "contact3")
        );

        List<AlertDTO> criticalAlerts = alerts.stream()
                .filter(alert -> "critica".equals(alert.getPriority()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AlertAllDTO(criticalAlerts));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert properly ignored.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Alert not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert Found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlertDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
    })
    @Operation(summary = "Get an alert by its id.")
    @GetMapping("/{alert_id}")
    public ResponseEntity<Optional<AlertDTO>> getAlert(@PathVariable("alert_id") String alert_id){
        List<AlertDTO> alerts = Arrays.asList(
                new AlertDTO("1", "El cliente se desconecto", "critica", "agent1", "skill1", "queue1", "contact1"),
                new AlertDTO("2", "Sistema en riesgo de sobrecarga", "alta", "agent2", "skill2", "queue2", "contact2"),
                new AlertDTO("3", "Error de conexión", "critica", "agent3", "skill3", "queue3", "contact3")
        );

        Optional<AlertDTO> Alert = alerts.stream()
                .filter(alert -> alert.getId().equals(alert_id))
                .findFirst();

        return ResponseEntity.ok(Alert);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert updated.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlertAllDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
    })
    @Operation(summary = "Update an alert.")
    @PutMapping("/{alert_id}")
    public ResponseEntity<AlertDTO> putAlert(@PathVariable("alert_id") String alertId, @RequestBody AlertDTO alert) {
        return ResponseEntity.ok(alert);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts info found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlertInfoDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.", content = @Content),
    })
    @Operation(summary = "Get info or statistics about alerts.")
    @GetMapping("/info")
    public ResponseEntity<AlertInfoDTO> getAlertsInfo() {
        List<AlertDTO> alerts = Arrays.asList(
                new AlertDTO("1", "descripcion1", "critica", "agent1", "skill1", "queue1", "contact1"),
                new AlertDTO("2", "descripcion2", "critica", "agent2", "skill2", "queue2", "contact2"),
                new AlertDTO("3", "descripcion3", "media", "agent3", "skill3", "queue3", "contact3"),
                new AlertDTO("4", "descripcion4", "media", "agent3", "skill3", "queue3", "contact3"),
                new AlertDTO("5", "descripcion5", "baja", "agent3", "skill3", "queue3", "contact3"),
                new AlertDTO("6", "descripcion6", "critica", "agent3", "skill3", "queue3", "contact3"),
                new AlertDTO("7", "descripcion7", "media", "agent3", "skill3", "queue3", "contact3"),
                new AlertDTO("8", "descripcion8", "baja", "agent3", "skill3", "queue3", "contact3"));

        Map<String, Integer> alertCount = new HashMap<>();
        alertCount.put("total", 0);
        alertCount.put("critica", 0);
        alertCount.put("media", 0);
        alertCount.put("baja", 0);

        for (AlertDTO alert : alerts) {
            String priority = alert.getPriority();
            alertCount.put(priority, alertCount.get(priority) + 1);
            alertCount.put("total", alertCount.get("total") + 1);
        }

        AlertInfoDTO alertInfo = new AlertInfoDTO(
                alertCount.get("total"),
                alertCount.get("critica"),
                alertCount.get("media"),
                alertCount.get("baja"));

        return ResponseEntity.ok(alertInfo);
    }
}
