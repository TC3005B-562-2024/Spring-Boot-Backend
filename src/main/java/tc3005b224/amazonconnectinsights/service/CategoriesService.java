package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.repository.CategoryRepository;

import java.util.Optional;

@Service
@Transactional
public class CategoriesService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category saveCategories(Byte id, Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    public Category findById(Byte id) {
        Optional<Category> categoriesOptional = categoryRepository.findById(id);

        if(categoriesOptional.isPresent()) {
            return categoriesOptional.get();
        }
        return null;
    }

    public void deleteCategories(Byte id) {
        categoryRepository.deleteById(id);
    }
}


