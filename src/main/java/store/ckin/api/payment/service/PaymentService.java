package store.ckin.api.payment.service;

import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.dto.response.PaymentResponseDto;

/**
 * 결제 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */
public interface PaymentService {

    /**
     * 결제 생성 메서드입니다.
     *
     * @param saleId     주문 ID
     * @param requestDto 결제 생성 요청 DTO
     */
    void createPayment(Long saleId, PaymentRequestDto requestDto);

    /**
     * 결제 조회 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 조회된 결제 응답 DTO
     */
    PaymentResponseDto getPayment(Long saleId);
}
