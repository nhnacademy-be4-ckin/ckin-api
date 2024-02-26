package store.ckin.api.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.repository.BookRepository;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public interface BookService {

    @Transactional(readOnly = true)
    Page<BookResponseDto> findByAuthorName(String authorName, Pageable pageable);

    @Transactional(readOnly = true)
    Page<BookResponseDto> findByBookTitle(String booktitle, Pageable pageable);

    @Transactional
    void createBook(BookCreateRequestDto requestDto);

    @Transactional
    void updateBook(Long bookId, BookModifyRequestDto requestDto);
}
