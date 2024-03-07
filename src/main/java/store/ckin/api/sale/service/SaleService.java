package store.ckin.api.sale.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.ckin.api.common.dto.PagedResponse;
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
     * 페이징 처리된 주문 목록을 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 주문 응답 DTO 리스트
     */
    PagedResponse<List<SaleResponseDto>> getSales(Pageable pageable);

    /**
     * 주문 상세 정보를 조회합니다.
     *
     * @param saleId 주문 ID
     * @return 주문 응답 DTO
     */
    SaleResponseDto getSaleDetail(Long saleId);


    /**
     * 주문 결제 상태를 결제 완료(PAID))로 변경합니다.
     * @param saleId 주문 ID
     */
    void updateSalePaymentPaidStatus(Long saleId);
}
