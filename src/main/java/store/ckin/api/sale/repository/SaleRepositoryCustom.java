package store.ckin.api.sale.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;

/**
 * 주문 Repository Custom 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 03.
 */

@NoRepositoryBean
public interface SaleRepositoryCustom {

    /**
     * 주문 상세 정보를 조회하는 메서드.
     *
     * @param saleId 주문 ID
     * @return 주문 응답 DTO
     */
    SaleResponseDto findBySaleId(Long saleId);

    /**
     * 주문 ID로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 주문 상세 정보와 주문한 책 정보 DTO
     */
    SaleWithBookResponseDto getSaleWithBook(Long saleId);


}
