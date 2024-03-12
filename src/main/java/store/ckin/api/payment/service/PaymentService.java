package store.ckin.api.payment.service;

import store.ckin.api.payment.dto.request.PaymentRequestDto;

/**
 * 결제 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */
public interface PaymentService {

    void createPayment(Long saleId, PaymentRequestDto requestDto);

}
