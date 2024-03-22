package store.ckin.api.review.exception;

/**
 * 이미지 저장에 실패한 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
public class SaveFileException extends RuntimeException {

    public SaveFileException() {
        super("이미지 저장에 실패하였습니다.");
    }
}
