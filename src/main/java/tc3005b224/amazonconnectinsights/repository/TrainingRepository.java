package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tc3005b224.amazonconnectinsights.models_sql.Alert;
import tc3005b224.amazonconnectinsights.models_sql.Training;

/**
 * Repository for Training entity
 * 
 * TODO: Change Long to the actual type of the identifier
 */
@Repository
public interface TrainingRepository extends CrudRepository<Training, Short> {
    Iterable<Training> findByIsActiveAndAlertsInAndDenominationIn(boolean active,
            Iterable<Alert> alerts, Iterable<String> denominations);

    Iterable<Training> findByIsActiveAndAlerts(boolean active, Iterable<Alert> alerts);

    Iterable<Training> findByIsActiveAndDenominationIn(boolean active, Iterable<String> denominations);

    Iterable<Training> findByIsActiveAndAlertsResourceArnIn(boolean active, Iterable<String> resourceArns);

    Iterable<Training> findByIsActive(boolean active);

    Iterable<Training> findByAlertsIn(Iterable<Alert> alerts);

    Iterable<Training> findByAlertsAndDenominationIn(Iterable<Alert> alerts, Iterable<String> denominations);

    Iterable<Training> findByDenominationIn(Iterable<String> denominations);
    
    Iterable<Training> findByAlertsResourceArnIn(Iterable<String> resourceArns);
}
