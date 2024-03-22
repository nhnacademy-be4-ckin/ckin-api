package store.ckin.api.wishlist.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 이미 위시리스트에 추가되었을 때 호출되는 Exception 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
public class WishListAlreadyExistsException extends GeneralAlreadyExistsException {

    public WishListAlreadyExistsException(Long memberId, Long bookId) {
        super(String.format("이미 위시리스트에 추가된 책입니다. (memberId: %d, bookId: %d)", memberId, bookId));
    }
}
