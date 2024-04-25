package tc3005b224.amazonconnectinsights.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;
import tc3005b224.amazonconnectinsights.service.CategoriesService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Byte categoryId) {
        CategoryDTO categoryDTO = categoriesService.findById(categoryId);
        if (categoryDTO != null) {
            return ResponseEntity.ok(categoryDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
