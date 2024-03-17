package store.ckin.api.deliverypolicy.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송비 정책 생성 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Getter
@NoArgsConstructor
public class DeliveryPolicyCreateRequestDto {

    @NotNull(message = "배송비를 입력해주세요.")
    @PositiveOrZero(message = "배송비는 0원 이상으로 입력해주세요.")
    private Integer deliveryPolicyFee;

    @NotNull(message = "배송비 무료 조건 금액을 입력해주세요.")
    @PositiveOrZero(message = "배송비 무료 조건 금액은 0원 이상으로 입력해주세요.")
    private Integer deliveryPolicyCondition;

    @NotNull(message = "배송비 정책 사용 여부를 선택해주세요.")
    private Boolean deliveryPolicyState;
}
