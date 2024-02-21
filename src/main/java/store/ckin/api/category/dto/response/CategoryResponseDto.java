package store.ckin.api.category.dto.response;

import javax.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * CategoryResponseDto.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Getter
public class CategoryResponseDto {

    private Long categoryId;

    private String categoryName;

    @Builder
    public CategoryResponseDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
