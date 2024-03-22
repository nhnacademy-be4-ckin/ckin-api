package store.ckin.api.advice.exception;

/**
 * 잘못된 요청이 발생했을 때 발생하는 예외를 전역으로 처리하기 위한 클래스.
 *
 * @author 정승조
 * @version 2024. 03. 22.
 */
public class GeneralBadRequestException extends RuntimeException {

    public GeneralBadRequestException(String message) {
        super(message);
    }
}
