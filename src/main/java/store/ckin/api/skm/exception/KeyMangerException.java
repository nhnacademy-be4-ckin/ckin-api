package store.ckin.api.skm.exception;

/**
 * KeyManager 에서 발생한 Exception 클래스 입니다.
 *
 * @author 김준현
 * @version 2024. 02. 19
 */
public class KeyMangerException extends RuntimeException {
    public KeyMangerException(String message) {
        super(message);
    }
}

