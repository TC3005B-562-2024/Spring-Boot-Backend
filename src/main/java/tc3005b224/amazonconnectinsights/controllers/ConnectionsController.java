package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.connections.ConnectionDTO;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.service.ConnectionsService;

import java.util.List;

@RestController
@RequestMapping("/connections")
public class ConnectionsController {
    @Autowired
    private ConnectionsService connectionsService;

    @Operation(
            summary = "Returns an Iterable of all the instances of Connection in the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connections Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Connection.class)))
                            }
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<Connection>> getAllConnections() {
        Iterable<Connection> connection = connectionsService.findAll();
        return ResponseEntity.ok(connection);
    }

    @Operation(
            summary = "Returns a Connection given its connectionId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Connection.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
            }
    )
    @GetMapping("/{connectionId}")
    public ResponseEntity<Connection> getConnectionById(@PathVariable Short connectionId) {
        Connection connection = connectionsService.findById(connectionId);
        if (connection != null) {
            return ResponseEntity.ok(connection);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Adds a new Connection into the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Connection created.",
                            content = {@Content(
                                    mediaType = "application/json"
                            )
                            }
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Connection> createConnection(@RequestBody ConnectionDTO connectionDTO) {
        Connection connection = connectionsService.createConnection(connectionDTO);
        return new ResponseEntity<>(connection, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Updates all the attributes of the Connection that are not set as null in the RequestBody's ConnectionDTO.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connection Updated."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
            }
    )
    @PutMapping("/{connectionId}")
    public ResponseEntity<Connection> updateConnection(@PathVariable Short connectionId, @RequestBody ConnectionDTO connectionDTO) {
        Connection updatedConnection = connectionsService.updateConnection(connectionId, connectionDTO);
        if (updatedConnection != null) {
            return ResponseEntity.ok(updatedConnection);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Deletes a Connection given its connectionId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connection Deleted."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
            }
    )
    @DeleteMapping("/{connectionId}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Short connectionId) {
        boolean isDeleted = connectionsService.deleteConnection(connectionId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
