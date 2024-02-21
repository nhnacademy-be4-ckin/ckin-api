package store.ckin.api.author.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
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
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    public Page<AuthorResponseDto> findAllAuthors(Pageable pageable) {
        Pageable sortedByAuthorName = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("authorName"));

        Page<Author> authors = authorRepository.findAll(sortedByAuthorName);
        return authors.map(author -> AuthorResponseDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .build());
    }


    @Override
    public List<AuthorResponseDto> findAuthorsByName(String name) {
        return authorRepository.findAuthorsByName(name);
    }

    @Override
    @Transactional
    public AuthorResponseDto createAuthor(AuthorCreateRequestDto authorCreateRequestDto) {
        Author author = Author.builder()
                .authorName(authorCreateRequestDto.getAuthorName())
                .build();
        author = authorRepository.save(author);

        return AuthorResponseDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .build();
    }


    @Override
    public AuthorResponseDto findAuthorById(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
        return AuthorResponseDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .build();
    }



    @Override
    @Transactional
    public AuthorResponseDto updateAuthor(Long authorId, AuthorModifyRequestDto authorModifyRequestDto) {
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        Author updatedAuthor = existingAuthor.toBuilder()
                .authorName(authorModifyRequestDto.getAuthorName())
                .build();
        updatedAuthor = authorRepository.save(updatedAuthor);

        return AuthorResponseDto.builder()
                .authorId(updatedAuthor.getAuthorId())
                .authorName(updatedAuthor.getAuthorName())
                .build();
    }



    @Override
    @Transactional
    public void deleteAuthor(Long authorId) {
        authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        authorRepository.deleteById(authorId);
    }

}

