package tc3005b224.amazonconnectinsights.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tc3005b224.amazonconnectinsights.models_sql.Connections;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connections, Short> {

}