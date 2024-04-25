package tc3005b224.amazonconnectinsights.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tc3005b224.amazonconnectinsights.models_sql.Categories;

@Repository//
public interface CategoriesRepository extends CrudRepository<Categories, Integer> {
}