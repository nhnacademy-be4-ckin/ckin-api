package store.ckin.api.category.exception;

/**
 * CategoryNotFoundException.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }

}