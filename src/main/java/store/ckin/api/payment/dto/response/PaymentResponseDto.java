package store.ckin.api.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 결제 조회 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */

@ToString
@Getter
@AllArgsConstructor
public class PaymentResponseDto {

    private Long paymentId;

    private Long saleId;

    private String paymentKey;

    private String paymentStatus;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    private String receiptUrl;
}
