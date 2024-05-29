package tc3005b224.amazonconnectinsights.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.queue.QueueCardDTO;
import tc3005b224.amazonconnectinsights.dto.queue.QueueDTO;
import tc3005b224.amazonconnectinsights.service.QueueService;

@RestController
@RequestMapping("/queues")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @Operation(
        summary = "Returns a list of all the Queues inside the instance.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Queues found.",
                content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = QueueCardDTO.class))
                )}
            )
        }
    )
    @GetMapping
    public ResponseEntity<Iterable<QueueCardDTO>> getAllQueues(@RequestParam(required = false) String resourceId,
            Principal principal) {
        try {
            Iterable<QueueCardDTO> queueCards = queueService.findAll(principal.getName(), resourceId);
            return ResponseEntity.ok(queueCards);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Returns all the information of a specific Queue.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Queue found.",
                content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = QueueDTO.class)
                )}
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Queue not found.",
                content = @Content
            )
        }
    )
    @GetMapping("/{queueId}")
    public ResponseEntity<?> getIndividualQueue(@PathVariable String queueId, Principal principal) {
        try {
            return ResponseEntity.ok(queueService.findById(principal.getName(), queueId));
        } catch (Exception e) {
            // Return error if there is an exception.
            ErrorResponse error = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()).build();
            return new ResponseEntity<>(error, error.getStatusCode());
        }
    }
}
