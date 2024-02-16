package store.ckin.api.category.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CategoryUpdateRequestDto.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
@Data
@NoArgsConstructor
public class CategoryUpdateRequestDto {

    private String categoryName;

}
