package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.repository.InsightRepository;

import java.util.Optional;

@Service
@Transactional
public class InsightsService {
    @Autowired
    private InsightRepository insightRepository;

    public Iterable<Insight> findAll() {
        return insightRepository.findAll();
    }

    public Insight saveInsight(Short id, Insight newInsight) {
        return insightRepository.save(newInsight);
    }

    public Insight findById(Short id) {
        Optional<Insight> insightsOptional = insightRepository.findById(id);

        if(insightsOptional.isPresent()) {
            return insightsOptional.get();
        }
        return null;
    }

    public void deleteInsight(Short id) {
        insightRepository.deleteById(id);
    }
}
