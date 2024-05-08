package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends CrudRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {
    List<Alert> findByConnectionIdentifierAndResourceContainingAndSolvedAndInsight_Category_PriorityAndInsight_Category_DenominationContaining(int connectionIdentifier, String resource, Boolean solved, int priority, String denomination);
    Optional<Alert> findById(Long id);
    List<Alert> findByConnectionIdentifierAndResourceAndSolvedAndInsight_Category_Priority(int connectionIdentifier, String resouce, boolean solved, int priority);
}
