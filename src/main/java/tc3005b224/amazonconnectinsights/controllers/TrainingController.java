package tc3005b224.amazonconnectinsights.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tc3005b224.amazonconnectinsights.dto.training.TrainingDTO;

@RestController
@RequestMapping("trainings")
public class TrainingController {

    /**
     * Get the list of all trainings data
     * 
     * TODO: Create training model in db.
     * 
     * @param agentId
     * @return List<TrainingDTO>
     * @author Diego Jacobo Djmr5
     * @see TrainingDTO
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found trainings data", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Couldn't connect to Database", content = @Content),
    })
    @Operation(summary = "Get the list of all trainings data")
    @GetMapping("")
    public ResponseEntity<List<TrainingDTO>> getTrainingsData(
            @RequestParam(required = false, name = "agentId") String agentId) {

        List<TrainingDTO> trainings = new ArrayList<TrainingDTO>();
        Date creationDate = new Date();
        Date completionDate = new Date();
        trainings.add(new TrainingDTO("1", "This is a sample description", "1", creationDate, completionDate, false));
        trainings.add(new TrainingDTO("2", "This is a sample description", "2", creationDate, completionDate, true));
        trainings.add(new TrainingDTO("3", "This is a sample description", "2", creationDate, completionDate, false));
        trainings.add(new TrainingDTO("4", "This is a sample description", "3", creationDate, completionDate, false));

        // Filter by agentId
        if (agentId != null) {
            List<TrainingDTO> filteredTrainings = new ArrayList<TrainingDTO>();
            for (TrainingDTO training : trainings) {
                if (training.getAgentId().equals(agentId)) {
                    filteredTrainings.add(training);
                }
            }
            return ResponseEntity.ok(filteredTrainings);
        }

        return ResponseEntity.ok(trainings);
    }

}
