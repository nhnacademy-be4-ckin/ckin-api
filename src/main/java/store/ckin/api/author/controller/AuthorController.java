package store.ckin.api.author.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.service.AuthorService;

/**
 * AuthorController.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    /**
     * Gets all authors.
     *
     * @return the all authors
     */
    @GetMapping
    public ResponseEntity<Page<AuthorResponseDto>> getAllAuthors(Pageable pageable) {
        Page<AuthorResponseDto> authors = authorService.findAllAuthors(pageable);
        return ResponseEntity.ok(authors);
    }


    /**
     * Gets authors by name.
     *
     * @param name the name
     * @return the authors by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<AuthorResponseDto>> getAuthorsByName(@RequestParam String name) {
        List<AuthorResponseDto> authors = authorService.findAuthorsByName(name);
        return ResponseEntity.ok(authors);
    }


    /**
     * Create author response entity.
     *
     * @param authorCreateRequestDto the author create request dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorCreateRequestDto authorCreateRequestDto) {
        AuthorResponseDto createdAuthor = authorService.createAuthor(authorCreateRequestDto);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    /**
     * Gets author by id.
     *
     * @param authorId the author id
     * @return the author by id
     */
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long authorId) {
        AuthorResponseDto authorResponseDto = authorService.findAuthorById(authorId);
        return ResponseEntity.ok(authorResponseDto);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable Long authorId,
                                                          @Valid @RequestBody AuthorModifyRequestDto authorModifyRequestDto) {
        AuthorResponseDto updatedAuthor = authorService.updateAuthor(authorId, authorModifyRequestDto);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok().build();
    }



}
