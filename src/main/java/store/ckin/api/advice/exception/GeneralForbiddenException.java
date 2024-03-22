package store.ckin.api.advice.exception;

/**
 * 권한이 없을 때 발생하는 에러를 전역으로 처리하기 위한 클래스.
 *
 * @author 정승조
 * @version 2024. 03. 22.
 */
public class GeneralForbiddenException extends RuntimeException {

    public GeneralForbiddenException(String message) {
        super(message);
    }
}
