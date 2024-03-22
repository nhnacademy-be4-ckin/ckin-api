package store.ckin.api.deliverypolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 배송비 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Getter
@Builder
@AllArgsConstructor
public class DeliveryPolicyResponseDto {

    private Long deliveryPolicyId;

    private Integer deliveryPolicyFee;

    private Integer deliveryPolicyCondition;

    private Boolean deliveryPolicyState;
}
