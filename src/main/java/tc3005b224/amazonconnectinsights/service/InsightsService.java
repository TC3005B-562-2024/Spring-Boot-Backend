package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.dto.insights.InsightsDTO;
import tc3005b224.amazonconnectinsights.models_sql.Insight;
import tc3005b224.amazonconnectinsights.repository.InsightRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsightsService {
    @Autowired
    private InsightRepository insightRepository;

    public List<InsightsDTO> findAll() {
        List<Insight> insights = (List<Insight>) insightRepository.findAll();
        return insights.stream().map(this::toInsightDTO).collect(Collectors.toList());
    }   

    public Insight saveInsight(Short id, Insight newInsight) {
        return insightRepository.save(newInsight);
    }

    public InsightsDTO findById(short id) {
        Optional<Insight> insightsOptional = insightRepository.findById(id);

        if(insightsOptional.isPresent()) {
            return toInsightDTO(insightsOptional.get());
        }
        return null;
    }

    public void deleteInsight(Short id) {
        insightRepository.deleteById(id);
    }

    private InsightsDTO toInsightDTO(Insight insight) {
        InsightsDTO dto = new InsightsDTO();
        dto.setIdentifier(insight.getIdentifier());
        dto.setCategoryIdentifier(insight.getCategory().getIdentifier());
        dto.setDenomination(insight.getDenomination());
        dto.setDescription(insight.getDescription());
        dto.setDateRegistered(insight.getDateRegistered());
        dto.setDateUpdated(insight.getDateUpdated());
        dto.setActive(insight.getActive());
        return dto;
    }

    private Insight toInsight(InsightDTO dto) {
        Insight insight = new Insight();

        return insight;
    }
    
}
