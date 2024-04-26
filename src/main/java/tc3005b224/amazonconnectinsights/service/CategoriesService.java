package tc3005b224.amazonconnectinsights.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriesService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO findById(Byte id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return toCategoryDTO(categoryOptional.get());
        }
        return null;
    }

    public List<CategoryDTO> findAll() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        return categories.stream().map(this::toCategoryDTO).collect(Collectors.toList());
    }

    public boolean deleteCategory(Byte id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = toCategory(categoryDTO);
        category = categoryRepository.save(category);
        return toCategoryDTO(category);
    }

    private Category toCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setDenomination(dto.getDenomination());
        category.setDescription(dto.getDescription());
        category.setPriority(dto.getPriority());
        category.setDateRegistered(dto.getDateRegistered());
        category.setDateUpdated(dto.getDateUpdated());
        category.setActive(dto.getIsActive());
        return category;
    }

    private CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setIdentifier(category.getIdentifier());
        dto.setDenomination(category.getDenomination());
        dto.setDescription(category.getDescription());
        dto.setPriority(category.getPriority());
        dto.setDateRegistered(category.getDateRegistered());
        dto.setDateUpdated(category.getDateUpdated());
        dto.setIsActive(category.getActive());
        return dto;
    }
}


