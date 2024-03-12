package store.ckin.api.payment.facade;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.dto.response.PaymentSuccessResponseDto;
import store.ckin.api.payment.exception.PaymentAmountNotCorrectException;
import store.ckin.api.payment.exception.PaymentNotCompleteException;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.service.SaleService;

/**
 * 결제 퍼사드 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;

    private final SaleService saleService;

    @Transactional
    public PaymentSuccessResponseDto createPayment(PaymentRequestDto requestDto) {

        if (!("DONE".equals(requestDto.getPaymentStatus()))) {
            throw new PaymentNotCompleteException();
        }

        SaleResponseDto sale = saleService.getSaleDetailBySaleNumber(requestDto.getSaleNumber());

        if (!Objects.equals(sale.getSaleTotalPrice(), requestDto.getAmount())) {
            throw new PaymentAmountNotCorrectException();
        }

        paymentService.createPayment(sale.getSaleId(), requestDto);
        saleService.updateSalePaymentPaidStatus(sale.getSaleId());

        return PaymentSuccessResponseDto.builder()
                .saleNumber(sale.getSaleNumber())
                .receiverName(sale.getSaleReceiverName())
                .receiverContact(sale.getSaleReceiverContact())
                .address(sale.getSaleReceiverAddress())
                .saleTotalPrice(sale.getSaleTotalPrice())
                .deliveryDate(sale.getSaleDeliveryDate())
                .receiptUrl(requestDto.getReceiptUrl())
                .build();
    }
}
