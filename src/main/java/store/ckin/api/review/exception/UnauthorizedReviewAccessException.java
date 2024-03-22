package store.ckin.api.review.exception;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 03. 21.
 */
public class UnauthorizedReviewAccessException extends RuntimeException {
    public UnauthorizedReviewAccessException(Long memberId) {
        super(String.format("수정 권한이 없습니다: %d", memberId));
    }
}
