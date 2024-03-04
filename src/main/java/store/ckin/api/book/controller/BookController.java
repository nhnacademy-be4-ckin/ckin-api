package store.ckin.api.book.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookExtractionResponseDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.service.BookService;

/**
 * BookController 클래스입니다.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long bookId) {
        BookResponseDto bookResponseDto = bookService.findBookById(bookId);
        return ResponseEntity.ok(bookResponseDto);
    }

    @GetMapping("/search/by-author")
    public ResponseEntity<Page<BookListResponseDto>> findByAuthorName(@RequestParam String authorName,
                                                                      @PageableDefault(size = 10, sort = "bookPublicationDate", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        return ResponseEntity.ok(bookService.findByAuthorName(authorName, pageable));
    }

    @GetMapping("/search/by-title")
    public ResponseEntity<Page<BookListResponseDto>> findByBookTitle(@RequestParam String title,
                                                                     @PageableDefault(size = 10, sort = "bookPublicationDate", direction = Sort.Direction.DESC)
                                                                     Pageable pageable) {
        return ResponseEntity.ok(bookService.findByBookTitle(title, pageable));
    }

    @GetMapping("/search/by-category")
    public ResponseEntity<Page<BookListResponseDto>> findByCategoryId(@RequestParam Long categoryId,
                                                                      @PageableDefault(size = 10, sort = "bookPublicationDate", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        return ResponseEntity.ok(bookService.findByCategoryId(categoryId, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<BookListResponseDto>> findAllBooks(
            @PageableDefault(size = 10, sort = "bookPublicationDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(bookService.findAllBooks(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> createBook(@Valid @RequestBody BookCreateRequestDto requestDto) {
        bookService.createBook(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable Long bookId,
                                           @Valid @RequestBody BookModifyRequestDto requestDto) {
        bookService.updateBook(bookId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 도서에서 필요한 정보만 반환하는 메서드입니다.
     *
     * @param bookIds 도서 ID 리스트
     * @return 200(OK), 도서 정보 리스트
     */
    @GetMapping("/extraction")
    public ResponseEntity<List<BookExtractionResponseDto>> getExtractBookListByBookIds(
            @RequestParam("bookId") List<Long> bookIds) {
        return ResponseEntity.ok(bookService.getExtractBookListByBookIds(bookIds));
    }

}
