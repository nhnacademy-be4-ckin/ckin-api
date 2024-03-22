package store.ckin.api.review.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * ReviewNotFoundException 예외 클래스입니다.
 *
 * @author 나국로
 * @version 2024. 03. 01.
 */
public class ReviewNotFoundException extends GeneralNotFoundException {
    public ReviewNotFoundException(Long reviewId) {
        super(String.format("리뷰를 찾을 수 없습니다. [id = %d]", reviewId));
    }
}
