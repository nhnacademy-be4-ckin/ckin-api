package store.ckin.api.author.service.impl;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AuthorResponseDto> findAllAuthors(Pageable pageable) {
        Pageable sortedByAuthorName =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("authorName"));

        Page<Author> authors = authorRepository.findAll(sortedByAuthorName);
        return authors.map(author -> AuthorResponseDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AuthorResponseDto> findAuthorsByName(String name, Pageable pageable) {

        return authorRepository.findAuthorsByName(name, pageable);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorResponseDto findAuthorById(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(AuthorNotFoundException::new);
        return AuthorResponseDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthorResponseDto updateAuthor(Long authorId, AuthorModifyRequestDto authorModifyRequestDto) {
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(AuthorNotFoundException::new);

        existingAuthor.updateAuthor(authorModifyRequestDto.getAuthorName());

        return AuthorResponseDto.builder()
                .authorId(existingAuthor.getAuthorId())
                .authorName(existingAuthor.getAuthorName())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAuthor(Long authorId) {

        if (!authorRepository.existsById(authorId)) {
            throw new AuthorNotFoundException();
        }

        authorRepository.deleteById(authorId);
    }
}

