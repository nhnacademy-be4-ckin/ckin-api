package store.ckin.api.author.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorModifyRequestDto {
    @NotBlank(message = "작가 이름은 비어 있을 수 없습니다")
    @Size(max = 200, message = "작가 이름은 200자를 초과할 수 없습니다")
    private String authorName;
}