package store.ckin.api.cart.repository;

import store.ckin.api.cart.dto.CartIdResponseDto;

/**
 * Cart Querydsl 사용을 위한 Custom interface
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
public interface CartRepositoryCustom {
    CartIdResponseDto getCartIdByUserId(Long userId);
}
