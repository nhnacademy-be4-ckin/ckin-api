package store.ckin.api.cart.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * Cart 를 찾을 수 없을 때 발생하는 CartNotFoundException
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
public class CartNotFoundException extends GeneralNotFoundException {
    public CartNotFoundException(Long userId) {
        super(String.format("해당 유저의 장바구니가 존재하지 않습니다. [userId = %d]", userId));
    }
}
