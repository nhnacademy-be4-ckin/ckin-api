package store.ckin.api.book.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * BookNotFoundException 예외 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public class BookNotFoundException extends GeneralNotFoundException {

    public BookNotFoundException(Long bookId) {
        super(String.format("책(id: %d)을 찾을 수 없습니다", bookId));

    }
}
