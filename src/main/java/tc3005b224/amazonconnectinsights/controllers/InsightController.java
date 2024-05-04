package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import tc3005b224.amazonconnectinsights.dto.insights.InsightDTO;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.service.InsightService;

@RestController
@RequestMapping("/insights")
public class InsightController {
    @Autowired
    private InsightService insightService;

    @Operation(
            summary = "Returns an Iterable of all the instances of Insight in the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Insights Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Insight.class)))
                            }
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<Insight>> getAllInsights() {
        Iterable<Insight> insights = insightService.findAll();
        return ResponseEntity.ok(insights);
    }

    @Operation(
            summary = "Adds a new Insight into the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category created.",
                            content = {@Content(
                                    mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Insight category not found.",
                            content = {@Content(
                                    mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createInsight(@RequestBody InsightDTO insightDTO) {
        try {
            insightService.createInsight(insightDTO);
            return ResponseEntity.ok("Insight added successfully");
        }
        catch(Exception e) {
            // Return error 404 if there is an exception.
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @Operation(
            summary = "Returns an Insight given its insightId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Insight Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Insight.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
            }
    )
    @GetMapping ("/{insightId}")
    public ResponseEntity<Insight> getInsightsById(@PathVariable("insightId") Short insightId) {
        Insight insight = insightService.findById(insightId);
        if (insight != null) {
            return ResponseEntity.ok(insight);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Deletes an Insight given its insightId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Insight deleted."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
            }
    )
    @DeleteMapping("/{insightId}")
    public ResponseEntity<Void> deleteInsight(@PathVariable("insightId") Short insightId) {
        boolean isDeleted = insightService.deleteInsight(insightId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Updates all the attributes of the Insight that are not set as null in the RequestBody's InsightDTO.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Insight Updated."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found."
                    ),
            }
    )
    @PutMapping("/{insightId}")
    public ResponseEntity<?> updateConnection(@PathVariable("insightId") Short insightId, @RequestBody InsightDTO insightDTO) {
        Insight updatedInsight = insightService.updateInsight(insightId, insightDTO);
        if (updatedInsight != null) {
            return ResponseEntity.ok("Insight upated successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
