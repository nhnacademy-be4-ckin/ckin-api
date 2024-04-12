package store.ckin.api.cart.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 해당 유저의 장바구니 데이터가 이미 존재할 때 발생하는 Exception
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
public class CartAlreadyExistException extends GeneralAlreadyExistsException {
    public CartAlreadyExistException(Long memberId) {
        super(String.format("해당 유저의 장바구니가 이미 존재합니다. [memberId = %d]", memberId));
    }
}
