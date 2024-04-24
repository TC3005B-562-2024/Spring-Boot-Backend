package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import tc3005b224.amazonconnectinsights.models_sql.Insights;

public interface InsightsRepository extends CrudRepository<Insights, Integer> {

}