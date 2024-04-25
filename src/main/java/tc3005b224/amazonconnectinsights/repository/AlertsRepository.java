package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import tc3005b224.amazonconnectinsights.models_sql.Alerts;

public interface AlertsRepository extends CrudRepository<Alerts, Short> {

}
