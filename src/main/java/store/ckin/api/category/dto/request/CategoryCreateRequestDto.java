package store.ckin.api.category.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * CategoryCreateRequestDto.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequestDto {
    private Long parentCategoryId;
    @NotBlank(message = "등록할 카테고리명을 기입해주세요.")
    @Length(max = 10, message = "카테고리명의 길이가 맞지않습니다.")
    private String categoryName;
    private Integer categoryPriority;
}