package store.ckin.api.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.entity.Payment;
import store.ckin.api.payment.repository.PaymentRepository;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final SaleRepository saleRepository;


    @Override
    @Transactional
    public void createPayment(Long saleId, PaymentRequestDto requestDto) {

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        Payment payment =
                Payment.builder()
                        .sale(sale)
                        .paymentKey(requestDto.getPaymentKey())
                        .paymentStatus("DONE")
                        .requestedAt(requestDto.getRequestedAt())
                        .approvedAt(requestDto.getApprovedAt())
                        .build();

        paymentRepository.save(payment);
    }
}
