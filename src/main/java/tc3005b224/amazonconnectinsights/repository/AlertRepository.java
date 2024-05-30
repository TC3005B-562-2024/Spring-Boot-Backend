package tc3005b224.amazonconnectinsights.repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    Iterable<Alert> findByConnectionIdentifierAndResourceContainingAndSolvedIsNotNullAndInsight_Category_PriorityAndInsight_Category_DenominationContaining(
            int connectionIdentifier, String resource, int priority, String denominationAlike);

    Iterable<Alert> findByConnectionIdentifierAndResourceContainingAndSolvedAndInsight_Category_Priority(
            int connectionIdentifier, String resource, boolean solved, int priority);

    Iterable<Alert> findByConnectionIdentifierAndResourceContainingAndSolvedAndInsight_Category_PriorityAndInsight_Category_DenominationContaining(
            int connectionIdentifier, String resource, Boolean solved, int priority, String denominationAlike);

    Iterable<Alert> findByConnectionIdentifierAndResourceAndSolvedAndInsight_Category_Priority(
            int connectionIdentifier, String resource, boolean solved, int priority);

    @Query("SELECT MAX(a.insight.category.priority) FROM Alert a WHERE a.solved IS NULL AND a.connection.identifier = :connectionIdentifier")
    Optional<Integer> findHighestPriority(int connectionIdentifier);

    @Query("SELECT MAX(a.insight.category.priority) FROM Alert a WHERE a.solved IS NULL AND a.resource = :resource AND a.connection.identifier = :connectionIdentifier")
    Optional<Integer> findHighestPriorityByResource(String resource, int connectionIdentifier);

    Iterable<Alert> findByConnectionIdentifierAndResourceAndSolvedAndHasTraining(int connectionIdentifier, String resource, Boolean solved, Boolean hasTraining);

    Iterable<Alert> findByResourceAndInsightIdentifierAndDateRegisteredBetween(String resource, Short insightIdentifier, Date dateFrom, Date dateTo);
}
