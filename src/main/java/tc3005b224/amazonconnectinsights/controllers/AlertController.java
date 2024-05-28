package tc3005b224.amazonconnectinsights.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertHighPriorityDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.service.AlertService;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    @Autowired
    private AlertService alertService;

    @Operation(summary = "Returns an AlertPriorityDTO, which has multiple lists of alerts ordered by priority.", responses = {
            @ApiResponse(responseCode = "200", description = "Alerts Found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlertPriorityDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.") })
    @GetMapping
    public ResponseEntity<AlertPriorityDTO> postConnectionAlerts(
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "") String resource,
            @RequestParam(required = false, defaultValue = "false") String logs, @RequestBody Principal principal) {
        try {
            AlertPriorityDTO response = alertService.findAll(principal.getName(), category, resource, logs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Returns an Alert given its alertId.", responses = {
            @ApiResponse(responseCode = "200", description = "Alert Found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Alert.class))
            }),
            @ApiResponse(responseCode = "404", description = "The resource you requested could not be found."),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database.") })
    @GetMapping("/{alertIdentifier}")
    public ResponseEntity<?> getIndividualAlert(@PathVariable Long alertIdentifier, @RequestBody Principal principal) {
        Alert queriedAlert;
        try {
            queriedAlert = alertService.findByIdentifier(principal.getName(), alertIdentifier);
        } catch (Exception e) {
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }

        // Return error 404 if the alert is not found in the database.
        if (queriedAlert == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(queriedAlert);
    }

    @Operation(summary = "Adds a new alert into the database.", responses = {
            @ApiResponse(responseCode = "200", description = "Alert Added."),
            @ApiResponse(responseCode = "404", description = "Invalid connection, trainig or insight identifier."),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database."),
    }

    )
    @PostMapping("")
    public ResponseEntity<?> postAlert(@RequestBody AlertDTO dto, @RequestBody Principal principal) {
        try {
            alertService.saveAlert(principal.getName(), alertService.fromDTO(dto));
            return ResponseEntity.ok("Alert added successfully");
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Updates all the attributes of the alert that are not set as null in the RequestBody's AlertDTO.", responses = {
            @ApiResponse(responseCode = "200", description = "Alert Added."),
            @ApiResponse(responseCode = "404", description = "Invalid alert, connection, trainig or insight identifier."),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database."),
    })
    @PutMapping("/{alertIdentifier}")
    public ResponseEntity<?> putAlert(@PathVariable Long alertIdentifier, @RequestBody AlertDTO alertDTO, Principal principal) {
        Alert queriedAlert;
        try {
            queriedAlert = alertService.findByIdentifier(principal.getName(), alertIdentifier);
        } catch (Exception e) {
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }

        // Return error 404 if the alert is not found in the database.
        if (queriedAlert == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            try {
                alertService.updateAlert(principal.getName(), alertIdentifier, alertDTO);
                return ResponseEntity.ok("Alert upated successfully");
            } catch (Exception e) {
                // Return error 404 if there is an exception.
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @Operation(summary = "Deletes an alert given its alertIdentifier.", responses = {
            @ApiResponse(responseCode = "200", description = "Alert Added."),
            @ApiResponse(responseCode = "404", description = "Invalid alertIdentifier."),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database."),
    })
    @DeleteMapping("/{alertIdentifier}")
    public ResponseEntity<?> putAlert(@PathVariable Long alertIdentifier, @RequestBody Principal principal) {
        try {
            alertService.deleteById(principal.getName(), alertIdentifier);
            return ResponseEntity.ok("Alert deleted successfully");
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Ignores an alert by logically deleting it (sets isSolevd to false).", responses = {
            @ApiResponse(responseCode = "200", description = "Alert ignored successfully."),
            @ApiResponse(responseCode = "404", description = "Invalid alertIdentifier."),
    })
    @PostMapping("/{alertIdentifier}/ignore")
    public ResponseEntity<?> ignoreAlert(@PathVariable Long alertIdentifier, @RequestBody Principal principal) {
        try {
            alertService.ignoreById(principal.getName(), alertIdentifier);
            return ResponseEntity.ok("Alert ignored successfully.");
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Executes the action especified by the insight and the, ignores the alert.", responses = {
            @ApiResponse(responseCode = "200", description = "Alert accepted successfully."),
            @ApiResponse(responseCode = "404", description = "Invalid alertIdentifier."),
    })
    @PostMapping("/{alertIdentifier}/accept")
    public ResponseEntity<?> acceptAlert(@PathVariable Long alertIdentifier, @RequestBody Principal principal) {
        try {
            String alertInsightCategoryDenomination = alertService.acceptById(principal.getName(), alertIdentifier);
            return ResponseEntity.ok("Alert of type: " + alertInsightCategoryDenomination + ", accepted.");
        } catch (Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Returns the highest priority of the alerts.", responses = {
            @ApiResponse(responseCode = "200", description = "Highest priority found.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error."),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to database."),
    })
    @GetMapping("/highestPriority")
    public ResponseEntity<AlertHighPriorityDTO> getHighestPriority(@RequestParam(required = false) String resource, @RequestBody Principal principal) {
        AlertHighPriorityDTO priority = alertService.findHighestPriority(principal.getName(), resource);
        return ResponseEntity.ok(priority);
    }
}
