package store.ckin.api.author.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.author.dto.register.AuthorRegisterDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.service.AuthorService;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@RestController
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public List<AuthorResponseDto> getAllAuthors() {
        return authorService.findAllAuthors();
    }

    @GetMapping("/authors/search")
    public List<AuthorResponseDto> getAuthorsByName(@RequestParam String name) {
        return authorService.findAuthorsByName(name);
    }


    @PostMapping("/authors")
    public AuthorResponseDto registerAuthor(@RequestBody AuthorRegisterDto authorRegisterDto) {
        return authorService.registerAuthor(authorRegisterDto);
    }

}
