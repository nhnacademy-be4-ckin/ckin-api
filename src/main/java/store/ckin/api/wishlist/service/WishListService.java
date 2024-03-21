package store.ckin.api.wishlist.service;

import store.ckin.api.wishlist.domain.request.WishListRequestDto;

/**
 * Wish List 의 관한 로직을 처리하는 서비스 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
public interface WishListService {
    void createWishList(WishListRequestDto wishListRequestDto);

    void deleteWishList(WishListRequestDto wishListRequestDto);
}
