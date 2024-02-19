package store.ckin.api.deliverypolicy.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;

/**
 * 배송비 정책 레포지토리 테스트 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@DataJpaTest
class DeliveryPolicyRepositoryTest {

    @Autowired
    DeliveryPolicyRepository deliveryPolicyRepository;

    @Test
    @DisplayName("배송비 정책 저장 테스트")
    void testSaveDeliveryPolicy() {

        DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
                .deliveryPolicyFee(5000)
                .deliveryPolicyCondition(10000)
                .deliveryPolicyState(true)
                .build();

        DeliveryPolicy savedDeliveryPolicy = deliveryPolicyRepository.save(deliveryPolicy);

        Assertions.assertTrue(deliveryPolicyRepository.existsById(savedDeliveryPolicy.getDeliveryPolicyId()));
    }

}