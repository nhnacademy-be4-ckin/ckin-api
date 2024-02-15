package store.ckin.api.deliverypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;

/**
 * 배송비 정책을 관리하는 레포지토리입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */
public interface DeliveryPolicyRepository extends JpaRepository<DeliveryPolicy, Long> {
}
