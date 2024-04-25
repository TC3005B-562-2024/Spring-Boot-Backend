package tc3005b224.amazonconnectinsights.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tc3005b224.amazonconnectinsights.models_sql.Training;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long>{

}
