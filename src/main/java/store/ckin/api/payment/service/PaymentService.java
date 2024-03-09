package store.ckin.api.payment.service;

import store.ckin.api.payment.dto.request.PaymentRequestDto;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */
public interface PaymentService {

    void createPayment(Long saleId, PaymentRequestDto requestDto);

}
