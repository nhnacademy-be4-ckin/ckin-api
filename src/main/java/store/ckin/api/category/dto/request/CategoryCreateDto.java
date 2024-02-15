package store.ckin.api.category.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Data
public class CategoryCreateDto {
    private Long parentCategoryId; // 상위 카테고리의 ID
    private String categoryName;

    @Builder
    public CategoryCreateDto(Long parentCategoryId, String categoryName) {
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }
}