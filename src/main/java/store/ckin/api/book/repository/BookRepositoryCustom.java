package store.ckin.api.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.book.dto.response.BookResponseDto;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public interface BookRepositoryCustom {
    Page<BookResponseDto> findByAuthorName(String authorName, Pageable pageable);
    Page<BookResponseDto> findByBookTitleContaining(String bookTitle, Pageable pageable);


}
