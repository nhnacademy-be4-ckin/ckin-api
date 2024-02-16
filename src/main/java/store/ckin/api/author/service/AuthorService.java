package store.ckin.api.author.service;

import java.util.List;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
/**
 * AuthorService.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
public interface AuthorService {
    List<AuthorResponseDto> findAllAuthors();

    List<AuthorResponseDto> findAuthorsByName(String name);

    AuthorResponseDto createAuthor(AuthorCreateRequestDto authorCreateRequestDto);

    AuthorResponseDto findAuthorById(Long authorId);

}
