package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.models_sql.Connection;
import tc3005b224.amazonconnectinsights.repository.CategoryRepository;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Service that looks for a Category, given its id. If the Category is not found, returns a null.
    public Category findById(Short id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if(categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        return null;
    }

    // Service that returns an Iterable of all the categories in the database.
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Service that deletes a Category given its id. If the action is successful returns a true, else a false.
    public boolean deleteCategory(Short id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Service that instantiates a category, given a CategoryDTO and stores it in the DB.
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);
        return categoryRepository.save(category);
    }

    // Service that updates a category considering all the non-null attributes of CategoryDTO.
    public Category updateCategory(Short id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryOptional.get().updateFromDTO(categoryDTO);
            return categoryRepository.save(categoryOptional.get());
        }
        return null;
    }
}


