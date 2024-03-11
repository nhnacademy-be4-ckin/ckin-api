package store.ckin.api.payment.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 성공 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessResponseDto {

    private String saleNumber;

    private String receiverName;

    private String receiverContact;

    private String address;

    private Integer saleTotalPrice;

    private LocalDate deliveryDate;

    private String receiptUrl;
}
