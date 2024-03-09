package store.ckin.api.payment.facade;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.service.SaleService;

/**
 * 결제 퍼사드 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;

    private final SaleService saleService;

    @Transactional
    public void createPayment(PaymentRequestDto requestDto) {

        SaleResponseDto sale = saleService.getSaleDetailBySaleNumber(requestDto.getSaleNumber());

        // TODO : 예외 클래스 만들기
        if (!Objects.equals(sale.getSaleTotalPrice(), requestDto.getAmount())) {
            throw new IllegalArgumentException("결제 금액이 맞지 않습니다.");
        }

        if (!("DONE".equals(requestDto.getPaymentStatus()))) {
            throw new IllegalArgumentException("결제가 완료되지 않았습니다.");
        }

        paymentService.createPayment(sale.getSaleId(), requestDto);
    }
}
