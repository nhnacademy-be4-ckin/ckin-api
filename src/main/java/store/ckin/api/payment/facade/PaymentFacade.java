package store.ckin.api.payment.facade;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.member.service.MemberService;
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

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;

    private final SaleService saleService;

    private final MemberService memberService;

    /**
     * 결제 정보를 DB에 저장 후 성공 응답 DTO를 반환하는 메서드입니다.
     *
     * @param requestDto 결제 요청 DTO
     * @return 결제 성공 응답 DTO
     */
    @Transactional
    public PaymentSuccessResponseDto createPayment(PaymentRequestDto requestDto) {

        if (!("DONE".equals(requestDto.getPaymentStatus()))) {
            throw new PaymentNotCompleteException();
        }

        SaleResponseDto sale = saleService.getSaleBySaleNumber(requestDto.getSaleNumber());

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


    /**
     * 주문 결제 완료 시 회원일 경우 포인트를 적립하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     */
    @Transactional
    public void createRewardPoint(String saleNumber) {
        SaleResponseDto sale = saleService.getSaleBySaleNumber(saleNumber);

        if (Objects.nonNull(sale.getMemberEmail())) {
            memberService.updateRewardPoint(sale.getMemberEmail(), sale.getSaleTotalPrice());
        }
    }
}
