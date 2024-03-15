package store.ckin.api.deliverypolicy.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;
import store.ckin.api.deliverypolicy.entity.QDeliveryPolicy;

import java.util.List;
import java.util.Optional;

/**
 * 배송비 정책 Repository Querydsl 사용을 위한 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 19.
 */
public class DeliveryPolicyRepositoryImpl extends QuerydslRepositorySupport
        implements DeliveryPolicyRepositoryCustom {

    public DeliveryPolicyRepositoryImpl() {
        super(DeliveryPolicy.class);
    }

    @Override
    public Optional<DeliveryPolicy> findByState(boolean state) {
        QDeliveryPolicy deliveryPolicy = QDeliveryPolicy.deliveryPolicy;

        return Optional.ofNullable(from(deliveryPolicy)
                .where(deliveryPolicy.deliveryPolicyState.eq(state))
                .fetchOne());
    }

    @Override
    public List<DeliveryPolicyResponseDto> getDeliveryPolicies() {

        QDeliveryPolicy deliveryPolicy = QDeliveryPolicy.deliveryPolicy;

        return from(deliveryPolicy)
                .select(Projections.constructor(DeliveryPolicyResponseDto.class,
                        deliveryPolicy.deliveryPolicyId,
                        deliveryPolicy.deliveryPolicyFee,
                        deliveryPolicy.deliveryPolicyCondition,
                        deliveryPolicy.deliveryPolicyState))
                .fetch();
    }

    @Override
    public Optional<DeliveryPolicyResponseDto> getDeliveryPolicyById(Long id) {

        QDeliveryPolicy deliveryPolicy = QDeliveryPolicy.deliveryPolicy;

        return Optional.of(from(deliveryPolicy)
                .where(deliveryPolicy.deliveryPolicyId.eq(id))
                .select(Projections.constructor(DeliveryPolicyResponseDto.class,
                        deliveryPolicy.deliveryPolicyId,
                        deliveryPolicy.deliveryPolicyFee,
                        deliveryPolicy.deliveryPolicyCondition,
                        deliveryPolicy.deliveryPolicyState))
                .fetchOne());
    }

    @Override
    public Optional<DeliveryPolicyResponseDto> getActiveDeliveryPolicy() {

        QDeliveryPolicy deliveryPolicy = QDeliveryPolicy.deliveryPolicy;

        return Optional.ofNullable(from(deliveryPolicy)
                .where(deliveryPolicy.deliveryPolicyState.eq(true))
                .select(Projections.constructor(DeliveryPolicyResponseDto.class,
                        deliveryPolicy.deliveryPolicyId,
                        deliveryPolicy.deliveryPolicyFee,
                        deliveryPolicy.deliveryPolicyCondition,
                        deliveryPolicy.deliveryPolicyState))
                .fetchOne());
    }


}
