package store.ckin.api.category.dto.response;

import javax.persistence.Column;
import lombok.Builder;
import lombok.Data;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Data
public class CategoryResponseDto {

    private Long categoryId;

    private String categoryName;

    @Builder
    public CategoryResponseDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
