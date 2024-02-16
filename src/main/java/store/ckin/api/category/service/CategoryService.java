package store.ckin.api.category.service;

import java.util.List;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;

;

/**
 * CategoryService.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
public interface CategoryService {

    CategoryResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto);

    List<CategoryResponseDto> findTopCategories();


    List<CategoryResponseDto> findSubcategories(Long parentId);

    CategoryResponseDto updateCategory(Long categoryId, CategoryUpdateRequestDto categoryUpdateDto);


    void deleteCategory(Long categoryId);

}
