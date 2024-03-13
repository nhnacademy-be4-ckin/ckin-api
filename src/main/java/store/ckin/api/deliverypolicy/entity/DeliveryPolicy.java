package store.ckin.api.deliverypolicy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송비 정책 Entity.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Getter
@Entity
@Table(name = "DeliveryPolicy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryPolicy {

    @Id
    @Column(name = "deliverypolicy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryPolicyId;

    @Column(name = "deliverypolicy_fee")
    private Integer deliveryPolicyFee;

    @Column(name = "deliverypolicy_condition")
    private Integer deliveryPolicyCondition;

    @Column(name = "deliverypolicy_state")
    private Boolean deliveryPolicyState;


    /**
     * 배송비 정책 생성을 위한 빌더입니다.
     *
     * @param deliveryPolicyId        배송비 정책 ID
     * @param deliveryPolicyFee       배송비
     * @param deliveryPolicyCondition 배송비 무료 조건 금액
     * @param deliveryPolicyState     배송비 정책 사용 여부
     */
    @Builder
    public DeliveryPolicy(Long deliveryPolicyId, Integer deliveryPolicyFee, Integer deliveryPolicyCondition,
                          Boolean deliveryPolicyState) {
        this.deliveryPolicyId = deliveryPolicyId;
        this.deliveryPolicyFee = deliveryPolicyFee;
        this.deliveryPolicyCondition = deliveryPolicyCondition;
        this.deliveryPolicyState = deliveryPolicyState;
    }


    /**
     * 배송비 정책을 수정하는 메서드입니다.
     *
     * @param deliveryPolicyFee       배송비
     * @param deliveryPolicyCondition 배송비 무료 조건 금액
     * @param deliveryPolicyState     배송비 정책 사용 여부
     */
    public void update(Integer deliveryPolicyFee, Integer deliveryPolicyCondition, Boolean deliveryPolicyState) {
        this.deliveryPolicyFee = deliveryPolicyFee;
        this.deliveryPolicyCondition = deliveryPolicyCondition;
        this.deliveryPolicyState = deliveryPolicyState;
    }

    /**
     * 배송비 정책 사용 여부를 수정하는 메서드입니다.
     *
     * @param state 수정할 배송비 정책 사용 여부
     */
    public void updateState(boolean state) {
        this.deliveryPolicyState = state;
    }
}
