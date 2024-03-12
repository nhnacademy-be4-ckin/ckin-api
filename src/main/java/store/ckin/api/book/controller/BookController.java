package store.ckin.api.book.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookExtractionResponseDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.service.BookService;
import store.ckin.api.objectstorage.service.ObjectStorageService;

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
    private final ObjectStorageService objectStorageService;

    /**
     * 주어진 ID를 가진 도서의 정보를 반환합니다.
     *
     * @param bookId 도서 ID
     * @return 해당 도서에 대한 ResponseEntity 객체
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long bookId) {
        BookResponseDto bookResponseDto = bookService.findBookById(bookId);
        return ResponseEntity.ok(bookResponseDto);
    }

    /**
     * 작가 이름으로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param authorName 작가 이름
     * @param pageable   페이징 정보
     * @return 작가 이름으로 검색된 도서 목록에 대한 ResponseEntity 객체
     */
    @GetMapping("/search/by-author")
    public ResponseEntity<Page<BookListResponseDto>> findByAuthorName(@RequestParam String authorName,
                                                                      @PageableDefault(sort = "bookPublicationDate", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        return ResponseEntity.ok(bookService.findByAuthorName(authorName, pageable));
    }

    /**
     * 도서 제목으로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param title    도서 제목
     * @param pageable 페이징 정보
     * @return 도서 제목으로 검색된 도서 목록에 대한 ResponseEntity 객체
     */
    @GetMapping("/search/by-title")
    public ResponseEntity<Page<BookListResponseDto>> findByBookTitle(@RequestParam String title,
                                                                     @PageableDefault(sort = "bookPublicationDate", direction = Sort.Direction.DESC)
                                                                     Pageable pageable) {
        return ResponseEntity.ok(bookService.findByBookTitle(title, pageable));
    }

    /**
     * 카테고리 ID로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable   페이징 정보
     * @return 카테고리 ID로 검색된 도서 목록에 대한 ResponseEntity 객체
     */
    @GetMapping("/search/by-category")
    public ResponseEntity<Page<BookListResponseDto>> findByCategoryId(@RequestParam Long categoryId,
                                                                      @PageableDefault(sort = "bookPublicationDate", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        return ResponseEntity.ok(bookService.findByCategoryId(categoryId, pageable));
    }

    /**
     * 모든 도서를 페이징하여 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 모든 도서에 대한 ResponseEntity 객체
     */
    @GetMapping
    public ResponseEntity<Page<BookListResponseDto>> findAllBooks(
            @PageableDefault(sort = "bookPublicationDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(bookService.findAllBooks(pageable));
    }

    /**
     * 새로운 도서를 생성합니다.
     *
     * @param requestDto 도서 생성 요청 DTO
     * @param file       도서 썸네일 이미지 파일
     * @return 생성 성공시 상태 코드 201과 함께 ResponseEntity 반환
     * @throws IOException 파일 처리 중 발생하는 예외
     */

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> createBook(@Valid @RequestPart("requestDto") BookCreateRequestDto requestDto,
                                           @RequestPart("file") MultipartFile file)
            throws IOException {
        bookService.createBook(requestDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 주어진 ID의 도서 정보를 수정합니다.
     *
     * @param bookId     도서 ID
     * @param requestDto 도서 수정 요청 DTO
     * @return 수정 성공시 상태 코드 200과 함께 ResponseEntity 반환
     */
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


    /**
     * 도서 설명 이미지를 업로드하고 해당 URL을 반환합니다.
     *
     * @param file 업로드할 이미지 파일
     * @return 업로드된 이미지 URL을 포함하는 ResponseEntity 객체
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    @PostMapping("/upload/description")
    public ResponseEntity<String> uploadDescriptionImage(@RequestPart("file") MultipartFile file) throws IOException {
        String category = "description";
        String fileUrl = objectStorageService.saveFile(file, category).getFileUrl();

        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }

    /**
     * 주어진 도서 ID 리스트에 해당하는 도서의 카테고리 ID들을 조회합니다.
     *
     * @param bookIds 도서 ID 리스트
     * @return 도서 카테고리 ID 리스트를 포함하는 ResponseEntity 객체
     */
    @GetMapping("/categories/by-books")
    public ResponseEntity<List<Long>> getBookCategoryIdsByBookIds(@RequestParam List<Long> bookIds) {
        List<Long> categoryIds = bookService.getBookCategoryIdsByBookIds(bookIds);
        return ResponseEntity.ok(categoryIds);
    }

    /**
     * 주어진 ID의 도서 썸네일을 업데이트합니다.
     *
     * @param bookId    도서 ID
     * @param thumbnail 새 도서 썸네일 이미지 파일
     * @return 업데이트 성공시 상태 코드 200과 함께 ResponseEntity 반환
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    @PutMapping("/thumbnail/{bookId}")
    public ResponseEntity<Void> updateBookThumbnail(@PathVariable Long bookId,
                                                    @RequestPart("thumbnail") MultipartFile thumbnail)
            throws IOException {
        bookService.updateBookThumbnail(bookId, thumbnail);
        return ResponseEntity.ok().build();
    }


}
