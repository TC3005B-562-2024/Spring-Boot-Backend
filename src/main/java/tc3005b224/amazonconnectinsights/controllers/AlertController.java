package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertDTO;
import tc3005b224.amazonconnectinsights.dto.alerts.AlertPriorityDTO;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.repository.ConnectionRepository;
import tc3005b224.amazonconnectinsights.service.AlertService;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    @Autowired
    private AlertService alertService;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Operation(
            summary = "Returns an AlertPriorityDTO, which has multiple lists of alerts ordered by priority.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alerts Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlertPriorityDTO.class)
                            )
                    }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Couldn't connect to database."
                    ),
            }

    )
    @GetMapping("/connections/{connectionIdentifier}")
    public ResponseEntity<AlertPriorityDTO> postConnectionAlerts(@PathVariable int connectionIdentifier, @RequestParam(required = false, defaultValue = "") String denominationAlike) {
        AlertPriorityDTO response = alertService.findAll(connectionIdentifier , denominationAlike);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Returns an Alert given its alertId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alert Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Alert.class)
                            )
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Couldn't connect to database."
                    ),
            }

    )
    @GetMapping("/{alertIdentifier}")
    public ResponseEntity<Alert> getIndividualAlert(@PathVariable Long alertIdentifier) {
        Alert queriedAlert = alertService.findByIdentifier(alertIdentifier);

        // Return error 404 if the alert is not found in the database.
        if(queriedAlert == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(queriedAlert);
    }

    @Operation(
            summary = "Returns an AlertPriorityDTO, which has multiple lists of alerts ordered by priority.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alerts Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlertPriorityDTO.class)
                            )
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Couldn't connect to database."
                    ),
            }

    )
    @GetMapping("/connections/{connectionIdentifier}/resource/{resourceArn}")
    public ResponseEntity<AlertPriorityDTO> getResourceAlerts(@PathVariable int connectionIdentifier, @PathVariable String resourceArn) {
        AlertPriorityDTO response = alertService.findByResource(connectionIdentifier, resourceArn);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Adds a new alert into the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alert Added."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invalid connection, trainig or insight identifier."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Couldn't connect to database."
                    ),
            }

    )
    @PostMapping("")
    public ResponseEntity<?> postAlert(@RequestBody AlertDTO dto) {
        try {
            alertService.saveAlert(alertService.fromDTO(dto));
            return ResponseEntity.ok("Alert added successfully");
        }
        catch(Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Updates all the attributes of the alert that are not set as null in the RequestBody's AlertDTO.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alert Added."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invalid alert, connection, trainig or insight identifier."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Couldn't connect to database."
                    ),
            }
    )
    @PutMapping("/{alertIdentifier}")
    public ResponseEntity<?> putAlert(@PathVariable Long alertIdentifier, @RequestBody AlertDTO dto) {
        Alert queriedAlert = alertService.findByIdentifier(alertIdentifier);

        // Return error 404 if the alert is not found in the database.
        if(queriedAlert == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }else{
            try {
                alertService.saveAlert(alertService.updateAlert(queriedAlert, dto));
                return ResponseEntity.ok("Alert upated successfully");
            }
            catch(Exception e) {
                // Return error 404 if there is an exception.
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }
    }

    @Operation(
            summary = "Deletes an alert given its alertIdentifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alert Added."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invalid alertIdentifier."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal error."
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Couldn't connect to database."
                    ),
            }
    )
    @DeleteMapping("/{alertIdentifier}")
    public ResponseEntity<?> putAlert(@PathVariable Long alertIdentifier){
        try {
            alertService.deleteById(alertIdentifier);
            return ResponseEntity.ok("Alert deleted successfully");
        }
        catch(Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
