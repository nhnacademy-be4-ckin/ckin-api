package store.ckin.api.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.cart.entity.Cart;

/**
 * Cart 레포지토리 입니다
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

}
