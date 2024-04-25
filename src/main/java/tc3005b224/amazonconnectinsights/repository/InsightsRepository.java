package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tc3005b224.amazonconnectinsights.models_sql.Insights;

@Repository
public interface InsightsRepository extends CrudRepository<Insights, Integer> {

}