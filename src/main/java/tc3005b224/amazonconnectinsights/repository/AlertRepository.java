package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import tc3005b224.amazonconnectinsights.models_sql.Alert;

public interface AlertRepository extends CrudRepository<Alert, Long> {

}
