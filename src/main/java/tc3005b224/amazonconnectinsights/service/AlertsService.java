package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Alerts;
import tc3005b224.amazonconnectinsights.repository.AlertsRepository;

import java.util.Optional;

@Service
@Transactional
public class AlertsService {
    @Autowired
    private AlertsRepository alertsRepository;

    public Iterable<Alerts> findAll() {
        return alertsRepository.findAll();
    }

    public Alerts saveAlerts(Short id, Alerts newAlerts) {
        return alertsRepository.save(newAlerts);
    }

    public Alerts findById(Short id) {
        Optional<Alerts> alertsOptional = alertsRepository.findById(id);

        if(alertsOptional.isPresent()) {
            return alertsOptional.get();
        }
        return null;
    }

    public  void deleteInsight(Short id) {
        alertsRepository.deleteById(id);
    }
}
