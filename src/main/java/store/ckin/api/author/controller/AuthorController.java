package store.ckin.api.author.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.author.dto.request.AuthorCreateDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.service.AuthorService;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorResponseDto> getAllAuthors() {
        return authorService.findAllAuthors();
    }

    @GetMapping("/search")
    public List<AuthorResponseDto> getAuthorsByName(@RequestParam String name) {
        return authorService.findAuthorsByName(name);
    }


    @PostMapping
    public AuthorResponseDto createAuthor(@RequestBody AuthorCreateDto authorCreateDto) {
        return authorService.createAuthor(authorCreateDto);
    }

}
