package store.ckin.api.category.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * CategoryNotFoundException.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
public class CategoryNotFoundException extends GeneralNotFoundException {

    public CategoryNotFoundException(Long categoryId) {
        super(String.format("Category not found: %s", categoryId));
    }
}