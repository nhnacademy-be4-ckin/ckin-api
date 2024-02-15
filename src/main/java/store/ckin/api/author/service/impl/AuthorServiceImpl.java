package store.ckin.api.author.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.author.dto.register.AuthorRegisterDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.repository.AuthorRepository;
import store.ckin.api.author.service.AuthorService;

/**
 * {class name}.
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
    public AuthorResponseDto registerAuthor(AuthorRegisterDto authorRegisterDto) {
        Author author = Author.builder()
                .authorName(authorRegisterDto.getAuthorName())
                .build();
        author = authorRepository.save(author);

        return new AuthorResponseDto(author);
    }
}

