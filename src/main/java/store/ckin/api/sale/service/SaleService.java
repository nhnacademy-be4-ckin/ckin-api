package store.ckin.api.sale.service;

import java.util.List;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;

/**
 * 주문 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */
public interface SaleService {

    /**
     * 주문 생성 요청을 합니다.
     *
     * @param requestDto 주문 생성 요청 DTO
     * @return 생성된 주문 ID
     */
    Long createSale(SaleCreateNoBookRequestDto requestDto);

    /**
     * 모든 주문을 조회합니다.
     * @return 주문 응답 DTO 리스트
     */
    List<SaleResponseDto> getSales();
}
