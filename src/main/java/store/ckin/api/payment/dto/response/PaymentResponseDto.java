package store.ckin.api.payment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.ckin.api.payment.entity.PaymentStatus;

/**
 * 결제 조회 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */

@Getter
@AllArgsConstructor
public class PaymentResponseDto {

    private Long paymentId;

    private Long saleId;

    private String paymentKey;

    private PaymentStatus paymentStatus;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    private String receiptUrl;
}
