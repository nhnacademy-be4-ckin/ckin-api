package store.ckin.api.address.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 이미 등록된 주소일 때 호출되는 Exception 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
public class AddressAlreadyExistsException extends GeneralAlreadyExistsException {

    public AddressAlreadyExistsException() {
        super("이미 등록된 주소입니다.");
    }
}
