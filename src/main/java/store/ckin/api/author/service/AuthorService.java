package store.ckin.api.author.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
/**
 * AuthorService.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
public interface AuthorService {
    Page<AuthorResponseDto> findAllAuthors(Pageable pageable);

    List<AuthorResponseDto> findAuthorsByName(String name);

    AuthorResponseDto createAuthor(AuthorCreateRequestDto authorCreateRequestDto);

    AuthorResponseDto findAuthorById(Long authorId);

    AuthorResponseDto updateAuthor(Long authorId, AuthorModifyRequestDto authorModifyRequestDto);

    void deleteAuthor(Long authorId);

}
