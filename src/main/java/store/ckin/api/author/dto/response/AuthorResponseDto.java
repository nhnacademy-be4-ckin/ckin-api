package store.ckin.api.author.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AuthorResponseDto 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@Getter
@NoArgsConstructor
public class AuthorResponseDto {

    private Long authorId;

    private String authorName;

    @Builder
    public AuthorResponseDto(Long authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }
}
