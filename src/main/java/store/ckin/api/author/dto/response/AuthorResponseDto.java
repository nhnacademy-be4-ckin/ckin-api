package store.ckin.api.author.dto.response;

import lombok.Data;
import store.ckin.api.author.entity.Author;

/**
 * AuthorResponseDto 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@Data
public class AuthorResponseDto {

    private Long authorId;

    private String authorName;

    public AuthorResponseDto(Author author) {
        this.authorId = author.getAuthorId();
        this.authorName = author.getAuthorName();
    }
}
