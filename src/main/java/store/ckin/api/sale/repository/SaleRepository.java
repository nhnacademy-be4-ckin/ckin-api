package store.ckin.api.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.sale.entity.Sale;

/**
 * 주문 레포지토리 인터페이스 입니다.
 *
 * @author 정승조
 * @version 2024. 02. 26.
 */
public interface SaleRepository extends JpaRepository<Sale, Long>, SaleRepositoryCustom {
}
