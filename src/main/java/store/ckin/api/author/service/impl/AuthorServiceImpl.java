package store.ckin.api.author.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.exception.AuthorNotFoundException;
import store.ckin.api.author.repository.AuthorRepository;
import store.ckin.api.author.service.AuthorService;

/**
 * AuthorServiceImpl.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> new AuthorResponseDto(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorResponseDto> findAuthorsByName(String name) {
        return authorRepository.findAuthorsByName(name);
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorCreateRequestDto authorCreateRequestDto) {
        Author author = Author.builder()
                .authorName(authorCreateRequestDto.getAuthorName())
                .build();
        author = authorRepository.save(author);

        return new AuthorResponseDto(author);
    }

    @Override
    public AuthorResponseDto findAuthorById(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("해당 ID의 작가를 찾을 수 없습니다."));
        return new AuthorResponseDto(author);
    }
}

