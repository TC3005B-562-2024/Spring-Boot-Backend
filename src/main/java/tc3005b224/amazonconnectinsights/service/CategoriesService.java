package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Categories;
import tc3005b224.amazonconnectinsights.models_sql.Insights;
import tc3005b224.amazonconnectinsights.repository.CategoriesRepository;

import java.util.Optional;

@Service
@Transactional
public class CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    public Iterable<Categories> findAll() {
        return categoriesRepository.findAll();
    }

    public Categories saveCategories(Byte id, Categories newCategories) {
        return categoriesRepository.save(newCategories);
    }

    public Categories findById(Byte id) {
        Optional<Categories> categoriesOptional = categoriesRepository.findById(id);

        if(categoriesOptional.isPresent()) {
            return categoriesOptional.get();
        }
        return null;
    }

    public void deleteCategories(Byte id) {
        categoriesRepository.deleteById(id);
    }
}


}
