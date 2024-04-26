package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.insights.InsightsDTO;
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

    @GetMapping ("/{insightsId}")
    public ResponseEntity<InsightsDTO> getInsightsById(@PathVariable("insightsId") short insightsId) {
        InsightsDTO insightsDTO = insightsService.findById(insightsId);
        if (insightsDTO != null) {
            return ResponseEntity.ok(insightsDTO);
        }
        return ResponseEntity.notFound().build();
    }




}