package store.ckin.api.author.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * AuthorCreateRequestDto.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@Getter
@NoArgsConstructor
public class AuthorCreateRequestDto {
    @NotBlank(message = "작가 이름은 비어 있을 수 없습니다")
    @Size(max = 200, message = "작가 이름은 200자를 초과할 수 없습니다")
    private String authorName;
}