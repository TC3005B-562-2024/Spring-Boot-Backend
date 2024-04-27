package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.insights.InsightsDTO;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.service.InsightsService;

import java.util.List;

@RestController
@RequestMapping("/insights")
public class InsightsController {
    @Autowired
    private InsightsService insightsService;

    @GetMapping
    public ResponseEntity<List<InsightsDTO>> getAllCategories() {
        List<InsightsDTO> insights = insightsService.findAll();
        return ResponseEntity.ok(insights);
    }

    @PostMapping
    public ResponseEntity<InsightsDTO> createInsight(@RequestBody InsightsDTO insightsDTO) {
        insightsDTO = insightsService.createInsight(insightsDTO);
        return new ResponseEntity<>(insightsDTO, HttpStatus.CREATED);
    }

    @GetMapping ("/{insightsId}")
    public ResponseEntity<InsightsDTO> getInsightsById(@PathVariable("insightsId") short insightsId) {
        InsightsDTO insightsDTO = insightsService.findById(insightsId);
        if (insightsDTO != null) {
            return ResponseEntity.ok(insightsDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{insightsId}")
    public ResponseEntity<Void> deleteInsight(@PathVariable("insightsId") short insightsId) {
        boolean isDeleted = insightsService.deleteInsight(insightsId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }






}