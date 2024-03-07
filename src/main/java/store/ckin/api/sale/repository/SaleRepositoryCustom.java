package store.ckin.api.sale.repository;

import store.ckin.api.sale.dto.response.SaleResponseDto;

/**
 * 주문 Repository Custom 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 03.
 */
public interface SaleRepositoryCustom {

    /**
     * 주문 상세 정보를 조회하는 메서드.
     *
     * @param saleId 주문 ID
     * @return 주문 응답 DTO
     */
    SaleResponseDto findBySaleId(Long saleId);
}
