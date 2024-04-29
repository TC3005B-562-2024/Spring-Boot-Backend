package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.repository.AlertRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;

    public Iterable<Alert> findAll() {
        return alertRepository.findAll();
    }

    public Alert saveAlerts(Long id, Alert newAlert) {
        return alertRepository.save(newAlert);
    }

    public Alert findById(Long id) {
        Optional<Alert> alertsOptional = alertRepository.findById(id);

        if(alertsOptional.isPresent()) {
            return alertsOptional.get();
        }
        return null;
    }

    public  void deleteInsight(Long id) {
        alertRepository.deleteById(id);
    }
}
