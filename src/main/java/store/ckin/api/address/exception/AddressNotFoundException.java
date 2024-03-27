package store.ckin.api.address.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 조회한 주소가 없을 때 호출되는 Exception 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
public class AddressNotFoundException extends GeneralNotFoundException {

    public AddressNotFoundException() {
        super("주소를 찾을 수 없습니다.");
    }
}
