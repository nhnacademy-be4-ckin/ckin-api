package store.ckin.api.author.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.service.AuthorService;

import javax.validation.Valid;

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
     * 모든 작가를 페이징하여 조회하고 반환합니다.
     *
     * @param pageable 페이징 정보
     * @return 모든 작가에 대한 페이징된 ResponseEntity 객체
     */
    @GetMapping
    public ResponseEntity<Page<AuthorResponseDto>> getAllAuthors(Pageable pageable) {
        Page<AuthorResponseDto> authors = authorService.findAllAuthors(pageable);
        return ResponseEntity.ok(authors);
    }


    /**
     * 이름으로 작가를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param name     작가의 이름
     * @param pageable 페이징 정보
     * @return 이름으로 검색된 작가 목록에 대한 ResponseEntity 객체
     */
    @GetMapping("/search")
    public ResponseEntity<Page<AuthorResponseDto>> getAuthorsByName(@RequestParam String name,
                                                                    @PageableDefault Pageable pageable) {
        Page<AuthorResponseDto> authors = authorService.findAuthorsByName(name, pageable);
        return ResponseEntity.ok(authors);
    }


    /**
     * 새로운 작가를 생성하고 결과를 반환합니다.
     *
     * @param authorCreateRequestDto 작가 생성 요청 DTO
     * @return 생성된 작가에 대한 ResponseEntity 객체
     */
    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(
            @Valid @RequestBody AuthorCreateRequestDto authorCreateRequestDto) {
        AuthorResponseDto createdAuthor = authorService.createAuthor(authorCreateRequestDto);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    /**
     * 주어진 ID를 가진 작가의 정보를 조회하고 반환합니다.
     *
     * @param authorId 작가 ID
     * @return 조회된 작가에 대한 ResponseEntity 객체
     */
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long authorId) {
        AuthorResponseDto authorResponseDto = authorService.findAuthorById(authorId);
        return ResponseEntity.ok(authorResponseDto);
    }

    /**
     * 주어진 ID를 가진 작가의 정보를 수정하고 결과를 반환합니다.
     *
     * @param authorId               작가 ID
     * @param authorModifyRequestDto 작가 수정 요청 DTO
     * @return 수정된 작가에 대한 ResponseEntity 객체
     */
    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable Long authorId,
                                                          @Valid @RequestBody
                                                          AuthorModifyRequestDto authorModifyRequestDto) {
        AuthorResponseDto updatedAuthor = authorService.updateAuthor(authorId, authorModifyRequestDto);
        return ResponseEntity.ok(updatedAuthor);
    }

    /**
     * 주어진 ID를 가진 작가를 삭제하고 결과를 반환합니다.
     *
     * @param authorId 작가 ID
     * @return 작가 삭제에 대한 ResponseEntity 객체
     */
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok().build();
    }


}
