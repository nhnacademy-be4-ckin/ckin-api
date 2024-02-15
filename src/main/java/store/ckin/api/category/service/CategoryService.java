package store.ckin.api.category.service;

import java.util.List;
import store.ckin.api.category.dto.request.CategoryCreateDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;

;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
public interface CategoryService {

    CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto);

    List<CategoryResponseDto> findSubcategories(Long parentId);

}
