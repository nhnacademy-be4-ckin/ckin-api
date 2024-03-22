package store.ckin.api.advice.exception;

/**
 * 존재하지 않는 데이터일 때 발생하는 에러를 전역으로 처리하기 위한 클래스.
 *
 * @author 정승조
 * @version 2024. 03. 22.
 */
public class GeneralNotFoundException extends RuntimeException {

    public GeneralNotFoundException(String message) {
        super(message);
    }
}
