package store.ckin.api.cart.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.cart.dto.CartIdResponseDto;
import store.ckin.api.cart.entity.Cart;
import store.ckin.api.cart.entity.QCart;

/**
 * CartRepositoryCustom 구현체
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
public class CartRepositoryImpl extends QuerydslRepositorySupport implements CartRepositoryCustom{
    public CartRepositoryImpl() {
        super(Cart.class);
    }

    @Override
    public CartIdResponseDto getCartIdByMemberId(Long memberId) {
        QCart cart = QCart.cart;

        return from(cart)
                .select(Projections.constructor(CartIdResponseDto.class,
                        cart.cartId))
                .where(cart.member.id.eq(memberId))
                .fetchOne();
    }
}
