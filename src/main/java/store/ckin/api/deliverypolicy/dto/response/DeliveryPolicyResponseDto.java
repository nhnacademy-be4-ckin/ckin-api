package store.ckin.api.deliverypolicy.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;

/**
 * 배송비 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryPolicyResponseDto {

    private Long deliveryPolicyId;

    private Integer deliveryPolicyFee;

    private Integer deliveryPolicyCondition;

    private Boolean deliveryPolicyState;

    @Builder
    public DeliveryPolicyResponseDto(Long deliveryPolicyId, Integer deliveryPolicyFee, Integer deliveryPolicyCondition,
                                     Boolean deliveryPolicyState) {
        this.deliveryPolicyId = deliveryPolicyId;
        this.deliveryPolicyFee = deliveryPolicyFee;
        this.deliveryPolicyCondition = deliveryPolicyCondition;
        this.deliveryPolicyState = deliveryPolicyState;
    }


    public static DeliveryPolicyResponseDto toDto(DeliveryPolicy deliveryPolicy) {
        return DeliveryPolicyResponseDto.builder()
                .deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
                .deliveryPolicyFee(deliveryPolicy.getDeliveryPolicyFee())
                .deliveryPolicyCondition(deliveryPolicy.getDeliveryPolicyCondition())
                .deliveryPolicyState(deliveryPolicy.getDeliveryPolicyState())
                .build();
    }
}
