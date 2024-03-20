package store.ckin.api.review.exception;

/**
 * ReviewNotFoundException 예외 클래스입니다.
 *
 * @author 나국로
 * @version 2024. 03. 01.
 */
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long reviewId) {
        super(String.format("Review not found: %s", reviewId));
    }
}
