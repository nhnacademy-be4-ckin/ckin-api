package store.ckin.api.deliverypolicy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;
import store.ckin.api.deliverypolicy.entity.QDeliveryPolicy;

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

        return Optional.ofNullable(
                from(deliveryPolicy)
                        .where(deliveryPolicy.deliveryPolicyState.eq(state))
                        .fetchOne());
    }
}
