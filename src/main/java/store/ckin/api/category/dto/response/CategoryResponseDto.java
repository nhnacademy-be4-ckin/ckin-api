package store.ckin.api.category.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CategoryResponseDto.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Getter
@NoArgsConstructor
public class CategoryResponseDto {

    private Long categoryId;

    private String categoryName;

    @Builder
    public CategoryResponseDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
