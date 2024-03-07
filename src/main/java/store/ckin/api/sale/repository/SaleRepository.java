package store.ckin.api.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.sale.entity.Sale;

/**
 * 주문 레포지토리 인터페이스 입니다.
 *
 * @author 정승조
 * @version 2024. 02. 26.
 */
public interface SaleRepository extends JpaRepository<Sale, Long>, SaleRepositoryCustom {

    /**
     * 주문 목록을 생성된 ID의 내림차순으로 페이징처리하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 주문 목록
     */
    Page<Sale> findAllByOrderBySaleIdDesc(Pageable pageable);
}
