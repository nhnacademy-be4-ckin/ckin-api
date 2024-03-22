package store.ckin.api.author.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * AuthorNotFoundException 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
public class AuthorNotFoundException extends GeneralNotFoundException {


    /**
     * Instantiates a new Author not found exception.
     *
     * @param authorId the author id
     */
    public AuthorNotFoundException(Long authorId) {
        super(String.format("작가를 찾을 수 없습니다 [ID = %d]", authorId));
    }

}