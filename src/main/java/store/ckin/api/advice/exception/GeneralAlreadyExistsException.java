package store.ckin.api.advice.exception;

/**
 * 이미 존재하는 데이터일 때 발생하는 에러를 전역으로 처리하기 위한 클래스.
 *
 * @author 정승조
 * @version 2024. 03. 22.
 */
public class GeneralAlreadyExistsException extends RuntimeException {

    public GeneralAlreadyExistsException(String message) {
        super(message);
    }
}
