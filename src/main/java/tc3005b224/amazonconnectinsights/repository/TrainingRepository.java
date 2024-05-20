package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.models_sql.Training;

/**
 * Repository for Training entity
 */
@Repository
public interface TrainingRepository extends CrudRepository<Training, Short> {
    Iterable<Training> findByIsActive(boolean active);

    Iterable<Training> findByAlerts(Alert alert);

    Iterable<Training> findByIsActiveAndAlertsResource(boolean active, String resource);

    Iterable<Training> findByAlertsResource(String resource);
}
