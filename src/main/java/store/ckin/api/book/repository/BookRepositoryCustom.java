package store.ckin.api.book.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.book.dto.response.BookExtractionResponseDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookMainPageResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.entity.Book;

/**
 * BookRepositoryCustom 인터페이스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@NoRepositoryBean
public interface BookRepositoryCustom {
    /**
     * 작가 이름으로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param authorName 작가 이름
     * @param pageable   페이징 정보
     * @return 작가 이름으로 검색된 도서 목록에 대한 페이지 객체
     */
    Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable);

    /**
     * 도서 제목으로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param bookTitle 도서 제목
     * @param pageable  페이징 정보
     * @return 도서 제목으로 검색된 도서 목록에 대한 페이지 객체
     */
    Page<BookListResponseDto> findByBookTitleContaining(String bookTitle, Pageable pageable);


    /**
     * 카테고리 ID로 도서를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable   페이징 정보
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
     * 주어진 도서 ID로 도서 정보를 조회합니다.
     *
     * @param bookId 도서 ID
     * @return 해당 도서 정보가 존재할 경우 그에 대한 Optional 객체
     */
    Optional<Book> findByBookId(Long bookId);

    /**
     * 도서에서 필요한 정보만 반환하는 메서드입니다.
     *
     * @param bookIds 도서 아이디 리스트
     * @return 도서 추출 정보 응답 DTO 리스트
     */
    List<BookExtractionResponseDto> getExtractBookListByBookIds(List<Long> bookIds);

    List<BookMainPageResponseDto> getMainPageResponseDtoByCategoryId(Long categoryId, Integer limit);

    List<BookMainPageResponseDto> getMainPageResponseDtoOrderByBookPublicationDate(Integer limit);

    /**
     * 태그 이름을 가진 도서 목록을 가져옵니다.
     *
     * @param limit   최대로 가져올 도서의 개수
     * @param tagName 태그 이름
     * @return 도서 목록
     */
    List<BookMainPageResponseDto> getMainPageBooksByTagName(Integer limit, String tagName);

    /**
     * 신간도서 페이지, 메인 페이지에 들어갈 신간 도서 목록을 페이지로 가져옵니다.
     *
     * @param pageable 페이지 정보
     * @return 신간 도서 페이지 목록
     */
    Page<BookResponseDto> getRecentPublished(Pageable pageable);


    /**
     * 인기도서, 추천도서 등을 태그 이름을 통해 가져옵니다.
     *
     * @param pageable 페이지 정보
     * @return 도서 페이지 목록
     */
    Page<BookResponseDto> getBookPageByTagName(Pageable pageable, String tagName);
}
