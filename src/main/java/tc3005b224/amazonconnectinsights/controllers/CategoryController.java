package tc3005b224.amazonconnectinsights.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tc3005b224.amazonconnectinsights.dto.category.CategoryDTO;
import tc3005b224.amazonconnectinsights.models_sql.Category;
import tc3005b224.amazonconnectinsights.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(
            summary = "Returns an Iterable of all the instances of Category in the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Alerts Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Category.class)))
                            }
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<Category>> getAllCategories() {
        Iterable<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Adds a new Category into the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created.",
                            content = {@Content(
                                    mediaType = "application/json"
                                    )
                            }
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Alert added successfully");
    }

    @Operation(
            summary = "Returns a Category given its categoryId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category Found.",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Category.class))
                            }
                    ),
                    @ApiResponse(
                    responseCode = "404",
                    description = "The resource you requested could not be found."
                    ),
            }
    )
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Short categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Deletes a category given its categoryId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category deleted."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The resource you requested could not be found."
                    ),
            }
    )
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Short categoryId) {
        boolean isDeleted = categoryService.deleteCategory(categoryId);
        if (isDeleted) {
            return ResponseEntity.ok("Category deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Updates all the attributes of the category that are not set as null in the RequestBody's CategoryDTO.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category Updated."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found."
                    ),
            }
    )
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Short categoryId, @RequestBody CategoryDTO categoryDTO) {
        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        if (updatedCategory != null) {
            return ResponseEntity.ok("Alert upated successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
