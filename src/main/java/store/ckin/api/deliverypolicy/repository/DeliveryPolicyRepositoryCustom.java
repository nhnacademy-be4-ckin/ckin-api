package store.ckin.api.deliverypolicy.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;

/**
 * 배송비 정책 Repository Querydsl 사용할 메서드가 있는 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 19.
 */

@NoRepositoryBean
public interface DeliveryPolicyRepositoryCustom {

    Optional<DeliveryPolicy> findByState(boolean b);

    List<DeliveryPolicyResponseDto> getDeliveryPolicies();

    Optional<DeliveryPolicyResponseDto> getDeliveryPolicyById(Long id);

    Optional<DeliveryPolicyResponseDto> getActiveDeliveryPolicy();
}
