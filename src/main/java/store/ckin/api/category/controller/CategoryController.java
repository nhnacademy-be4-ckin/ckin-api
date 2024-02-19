package store.ckin.api.category.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.service.CategoryService;

/**
 * CategoryController 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    /**
     * Create category response entity.
     *
     * @param categoryCreateRequestDto the category create request dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto) {
        categoryService.createCategory(categoryCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Gets top categories.
     *
     * @return the top categories
     */
    @GetMapping("/top")
    public ResponseEntity<List<CategoryResponseDto>> getTopCategories() {
        List<CategoryResponseDto> topCategories = categoryService.findTopCategories();
        return ResponseEntity.ok(topCategories);
    }


    /**
     * Gets subcategories.
     *
     * @param parentId the parent id
     * @return the subcategories
     */
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryResponseDto>> getSubcategories(@PathVariable Long parentId) {
        List<CategoryResponseDto> subcategories = categoryService.findSubcategories(parentId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * Update category response entity.
     *
     * @param categoryId               the category id
     * @param categoryUpdateRequestDto the category update request dto
     * @return the response entity
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId,
                                               @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto) {
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete category response entity.
     *
     * @param categoryId the category id
     * @return the response entity
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
