package store.ckin.api.payment.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 요청 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Getter
@AllArgsConstructor
public class PaymentRequestDto {

    private String paymentKey;

    private String saleNumber;

    private String paymentStatus;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    private Integer amount;

    private String receiptUrl;
}
