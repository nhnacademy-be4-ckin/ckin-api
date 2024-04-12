package store.ckin.api.cart.service;

import store.ckin.api.cart.dto.CartCreateRequestDto;
import store.ckin.api.cart.dto.CartIdResponseDto;

/**
 * Cart CRUD 를 제공하는 서비스 인터페이스
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
public interface CartService {
    void createCart(CartCreateRequestDto cartCreateRequestDto);
    CartIdResponseDto readCartId(Long userId);
}
