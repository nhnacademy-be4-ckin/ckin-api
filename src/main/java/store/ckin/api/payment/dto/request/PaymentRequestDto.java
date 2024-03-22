package store.ckin.api.payment.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.payment.entity.PaymentStatus;

/**
 * 결제 요청 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Getter
@NoArgsConstructor
public class PaymentRequestDto {

    @NotBlank(message = "결제 키는 필수입니다.")
    private String paymentKey;

    @NotBlank(message = "결제 번호는 필수입니다.")
    private String saleNumber;

    @NotNull(message = "결제 상태는 필수입니다.")
    private PaymentStatus paymentStatus;

    @NotNull(message = "결제 요청 시간은 필수입니다.")
    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    @NotNull(message = "결제 금액은 필수입니다.")
    private Integer amount;

    @NotBlank(message = "영수증 URL은 필수입니다.")
    private String receiptUrl;
}
