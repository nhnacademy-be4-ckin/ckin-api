package store.ckin.api.review.exception;

/**
 * 리뷰 수정 권한이 없을 때 발생하는 Exception 클래스 입니다.
 *
 * @author 나국로
 * @version 2024. 03. 21.
 */
public class UnauthorizedReviewAccessException extends RuntimeException {
    public UnauthorizedReviewAccessException() {
        super("수정 권한이 없습니다.");
    }
}
