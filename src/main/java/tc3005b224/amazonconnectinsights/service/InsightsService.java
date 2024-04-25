package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Insights;
import tc3005b224.amazonconnectinsights.repository.InsightsRepository;

import java.util.Optional;

@Service
@Transactional
public class InsightsService {
    @Autowired
    private InsightsRepository insightsRepository;

    public Iterable<Insights> findAll() {
        return insightsRepository.findAll();
    }

    public Insights saveInsight(Short id, Insights newInsight) {
        return insightsRepository.save(newInsight);
    }

    public Insights findById(Short id) {
        Optional<Insights> insightsOptional = insightsRepository.findById(id);

        if(insightsOptional.isPresent()) {
            return insightsOptional.get();
        }
        return null;
    }

    public void deleteInsight(Short id) {
        insightsRepository.deleteById(id);
    }
}
