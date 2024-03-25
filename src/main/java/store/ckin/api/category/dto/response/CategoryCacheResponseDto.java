package store.ckin.api.category.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CategoryCacheResponseDto.
 *
 * @author 나국로
 * @version 2024. 03. 24.
 */
@Getter
@NoArgsConstructor
public class CategoryCacheResponseDto {


    private Long categoryId;

    private Long parentCategoryId;

    private String categoryName;

    private Integer categoryPriority;

    @Builder
    public CategoryCacheResponseDto(Long categoryId, Long parentCategoryId, String categoryName,
                                    Integer categoryPriority) {
        this.categoryId = categoryId;
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
        this.categoryPriority = categoryPriority;
    }

}
