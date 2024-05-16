package tc3005b224.amazonconnectinsights.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tc3005b224.amazonconnectinsights.dto.insights.InsightDTO;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.repository.CategoryRepository;
import tc3005b224.amazonconnectinsights.repository.InsightRepository;

@Service
@Transactional
public class InsightService {
    @Autowired
    private InsightRepository insightRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // Service that returns an Iterable of all the insights in the database.
    public Iterable<Insight> findAll() {
        return insightRepository.findAll();
    }

    // Service that looks for an Insight, given its id. If the Insight is not found, returns a null.
    public Insight findById(short id) {
        Optional<Insight> insightsOptional = insightRepository.findById(id);
        if(insightsOptional.isPresent()) {
            return insightsOptional.get();
        }
        return null;
    }

    // Service that deletes an Insight given its id. If the action is successful returns a true, else a false.
    public boolean deleteInsight(Short id) {
        if (insightRepository.existsById(id)) {
            insightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Service that instantiates an Insight, given an InsightDTO and stores it in the DB.
    public Insight createInsight(InsightDTO insightDTO) {
        Category category = categoryRepository.findById(insightDTO.getCategoryIdentifier())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + insightDTO.getCategoryIdentifier()));

        Insight insight = new Insight(insightDTO, category);
        return insightRepository.save(insight);
    }

    // Service that updates an Insight considering all the non-null attributes of InsightDTO.
    public Insight updateInsight(Short id, InsightDTO insightDTO) {
        Optional<Insight> insightsOptional = insightRepository.findById(id);
        if (insightsOptional.isPresent()) {
            insightsOptional.get().updateFromDTO(insightDTO);
            return insightRepository.save(insightsOptional.get());
        }
        return null;
    }
}
