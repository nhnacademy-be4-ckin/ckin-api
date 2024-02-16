package store.ckin.api.author.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
public class AuthorNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Author not found exception.
     *
     * @param message the message
     */
    public AuthorNotFoundException(String message) {
        super(message);
    }

}