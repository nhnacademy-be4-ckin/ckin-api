package store.ckin.api.deliverypolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;

/**
 * 배송비 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Builder
@Getter
@AllArgsConstructor
public class DeliveryPolicyResponseDto {

    private Long deliveryPolicyId;

    private Integer deliveryPolicyFee;

    private Integer deliveryPolicyCondition;

    private Boolean deliveryPolicyState;

    public static DeliveryPolicyResponseDto toDto(DeliveryPolicy deliveryPolicy) {
        return DeliveryPolicyResponseDto.builder()
                .deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
                .deliveryPolicyFee(deliveryPolicy.getDeliveryPolicyFee())
                .deliveryPolicyCondition(deliveryPolicy.getDeliveryPolicyCondition())
                .deliveryPolicyState(deliveryPolicy.getDeliveryPolicyState())
                .build();
    }
}
