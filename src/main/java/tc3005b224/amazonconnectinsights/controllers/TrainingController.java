package tc3005b224.amazonconnectinsights.controllers;

import java.util.Map;
import java.security.Principal;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.training.TrainingDTO;
import tc3005b224.amazonconnectinsights.dto.training.TrainingNoIdDTO;
import tc3005b224.amazonconnectinsights.service.TrainingsService;

@RestController
@RequestMapping("trainings")
public class TrainingController {

        @Autowired
        private TrainingsService trainingService;

        /**
         * Get the list of all trainings data
         * 
         * @param agentId
         * @return ListTrainingDTO
         * @author Diego Jacobo Djmr5
         * @throws BadRequestException
         * @see TrainingDTO
         */
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found trainings data", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TrainingDTO.class)))
                        }),
                        @ApiResponse(responseCode = "400", description = "Invalid filters or Too many filters", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
                        @ApiResponse(responseCode = "503", description = "Couldn't connect to Database", content = @Content),
        })
        @Operation(summary = "Get the list of all trainings data")
        @GetMapping("")
        public ResponseEntity<?> getTrainingsData(
                        @RequestParam(required = false, defaultValue = "") String resource,
                        @RequestParam(required = false, defaultValue = "") String alertId,
                        @RequestParam(required = false, defaultValue = "") String isActive,
                        @RequestBody(required = false) Principal principal) throws BadRequestException {
                try {
                        return ResponseEntity.ok(trainingService.findAll(resource, alertId, isActive));
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                }
        }

        /**
         * Get the training data by its id
         * 
         * @param trainingId
         * @return TrainingDTO
         * @author Diego Jacobo Djmr5
         * @throws NotFoundException
         * @see TrainingDTO
         */
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found training data", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
                        @ApiResponse(responseCode = "503", description = "Couldn't connect to Database", content = @Content),
        })
        @Operation(summary = "Get the training data by its id")
        @GetMapping("{trainingId}")
        public ResponseEntity<TrainingDTO> getTrainingData(@PathVariable("trainingId") Short trainingId)
                        throws NotFoundException {
                try {
                        return ResponseEntity.ok(trainingService.findById(trainingId));
                } catch (NotFoundException e) {
                        return ResponseEntity.notFound().build();
                }
        }

        /**
         * Save a new training data
         * 
         * @param newTraining
         * @return TrainingDTO
         * @author Diego Jacobo Djmr5
         * @see TrainingDTO
         */
        @Operation(summary = "Save a new training data", responses = {
                        @ApiResponse(responseCode = "200", description = "Training data saved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Invalid atributtes", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
                        @ApiResponse(responseCode = "503", description = "Couldn't connect to Database", content = @Content),
        })
        @PostMapping("")
        public ResponseEntity<TrainingDTO> saveTrainingData(@RequestBody TrainingNoIdDTO newTraining) {
                return ResponseEntity.ok(trainingService.saveTraining(newTraining));
        }

        /**
         * Update a training data by its id
         * 
         * @param trainingId
         * @return
         * @author Diego Jacobo Djmr5
         * @throws NotFoundException
         * @throws BadRequestException
         * @see TrainingDTO
         */
        @Operation(summary = "Update a training data by its id", responses = {
                        @ApiResponse(responseCode = "200", description = "Training data updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
                        @ApiResponse(responseCode = "503", description = "Couldn't connect to Database", content = @Content),
        })
        @PatchMapping("{trainingId}")
        public ResponseEntity<?> updateTrainingData(@PathVariable("trainingId") Short trainingId,
                        @RequestBody Map<String, Object> fieldsToUpdate) throws NotFoundException, BadRequestException {
                try {
                        return ResponseEntity.ok(trainingService.updateTraining(trainingId, fieldsToUpdate));
                } catch (NotFoundException e) {
                        return ResponseEntity.notFound().build();
                } catch (BadRequestException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                }
        }

        /**
         * Delete a training data by its id
         * 
         * @param trainingId
         * @return void
         * @author Diego Jacobo Djmr5
         * @see TrainingDTO
         */
        @Operation(summary = "Delete a training data by its id", responses = {
                        @ApiResponse(responseCode = "204", description = "Training data deleted", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
                        @ApiResponse(responseCode = "503", description = "Couldn't connect to Database", content = @Content),
        })
        @DeleteMapping("{trainingId}")
        public ResponseEntity<Void> deleteTrainingData(@PathVariable("trainingId") Short trainingId) {
                try {
                        trainingService.deleteTraining(trainingId);
                        return ResponseEntity.noContent().build();
                } catch (NotFoundException e) {
                        e.printStackTrace();
                        return ResponseEntity.notFound().build();
                }
        }
}
