package store.ckin.api.author.service;

import java.util.List;
import store.ckin.api.author.dto.request.AuthorCreateDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;

public interface AuthorService {
    List<AuthorResponseDto> findAllAuthors();

    List<AuthorResponseDto> findAuthorsByName(String name);

    AuthorResponseDto createAuthor(AuthorCreateDto authorCreateDto);
}
