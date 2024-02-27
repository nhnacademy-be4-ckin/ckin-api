package store.ckin.api.book.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.entity.Book;

/**
 * BookRepositoryCustom 인터페이스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public interface BookRepositoryCustom {
    Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable);

    Page<BookListResponseDto> findByBookTitleContaining(String bookTitle, Pageable pageable);


    Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable);

    Page<BookListResponseDto> findAllBooks(Pageable pageable);

    Optional<Book> findByBookId(Long bookId);
}
