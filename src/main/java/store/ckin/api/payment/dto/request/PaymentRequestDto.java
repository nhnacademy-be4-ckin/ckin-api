package store.ckin.api.payment.dto.request;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.ToString;

/**
 * 결제 요청 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@ToString
@Getter
public class PaymentRequestDto {

    private final String paymentKey;

    private final String saleNumber;

    private final String paymentStatus;

    private final LocalDateTime requestedAt;

    private final LocalDateTime approvedAt;

    private final Integer amount;

    private final String receiptUrl;

    /**
     * 토스에서 받은 결제 정보를 저장하는 DTO.
     *
     * @param paymentKey    결제 고유 키
     * @param saleNumber    주문 번호
     * @param paymentStatus 결제 상태
     * @param requestedAt   결제 요청 시간
     * @param approvedAt    결제 승인 시간
     * @param amount        결제 금액
     * @param receiptUrl    영수증 URL
     */
    public PaymentRequestDto(String paymentKey, String saleNumber, String paymentStatus, OffsetDateTime requestedAt,
                             OffsetDateTime approvedAt, Integer amount, String receiptUrl) {
        this.paymentKey = paymentKey;
        this.saleNumber = saleNumber;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt.toLocalDateTime();
        this.approvedAt = approvedAt.toLocalDateTime();
        this.amount = amount;
        this.receiptUrl = receiptUrl;
    }
}
