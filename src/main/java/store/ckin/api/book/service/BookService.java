package store.ckin.api.book.service;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookExtractionResponseDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;

/**
 * BookService 인터페이스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public interface BookService {

    /**
     * 작가 이름으로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param authorName 작가 이름
     * @param pageable 페이징 정보
     * @return 작가 이름으로 검색된 도서 목록에 대한 페이지 객체
     */
    Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable);

    /**
     * 도서 제목으로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param bookTitle 도서 제목
     * @param pageable 페이징 정보
     * @return 도서 제목으로 검색된 도서 목록에 대한 페이지 객체
     */
    Page<BookListResponseDto> findByBookTitle(String bookTitle, Pageable pageable);

    /**
     * 카테고리 ID로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 카테고리 ID로 검색된 도서 목록에 대한 페이지 객체
     */
    Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 모든 도서를 페이징하여 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 모든 도서에 대한 페이지 객체
     */
    Page<BookListResponseDto> findAllBooks(Pageable pageable);

    /**
     * 새로운 도서를 생성합니다.
     *
     * @param requestDto 도서 생성 요청 DTO
     * @param file 도서 썸네일 이미지 파일
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    void createBook(BookCreateRequestDto requestDto, MultipartFile file) throws IOException;

    /**
     * 주어진 ID의 도서 정보를 수정합니다.
     *
     * @param bookId 도서 ID
     * @param requestDto 도서 수정 요청 DTO
     */
    void updateBook(Long bookId, BookModifyRequestDto requestDto);

    /**
     * 주어진 ID를 가진 도서의 정보를 조회합니다.
     *
     * @param bookId 도서 ID
     * @return 도서 정보 응답 DTO
     */
    BookResponseDto findBookById(Long bookId);

    /**
     * 도서에서 필요한 정보만 반환하는 메서드입니다.
     *
     * @param bookIds 도서 아이디 리스트
     * @return 도서 추출 정보 응답 DTO 리스트
     */
    List<BookExtractionResponseDto> getExtractBookListByBookIds(List<Long> bookIds);

    /**
     * 주어진 도서 ID 리스트에 해당하는 도서의 카테고리 ID들을 조회합니다.
     *
     * @param bookIds 도서 ID 리스트
     * @return 도서 카테고리 ID 리스트
     */
    List<Long> getBookCategoryIdsByBookIds(List<Long> bookIds);

    /**
     * 주어진 ID의 도서 썸네일을 업데이트합니다.
     *
     * @param bookId 도서 ID
     * @param newThumbnail 새 도서 썸네일 이미지 파일
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    void updateBookThumbnail(Long bookId, MultipartFile newThumbnail) throws IOException;
}


