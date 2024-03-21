package store.ckin.api.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.wishlist.entity.WishList;

/**
 * WishList 에 관한 쿼리를 관리하는 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
public interface WishListRepository extends JpaRepository<WishList, Long> {
    void deleteByPk(WishList.PK pk);
}
