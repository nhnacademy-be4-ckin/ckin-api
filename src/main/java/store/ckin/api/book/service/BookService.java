package store.ckin.api.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;

/**
 * BookService 인터페이스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public interface BookService {

    Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable);

    Page<BookListResponseDto> findByBookTitle(String bookTitle, Pageable pageable);

    Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable);

    Page<BookListResponseDto> findAllBooks(Pageable pageable);

    void createBook(BookCreateRequestDto requestDto);

    void updateBook(Long bookId, BookModifyRequestDto requestDto);

    BookResponseDto findBookById(Long bookId);
}
