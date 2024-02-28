package store.ckin.api.deliverypolicy.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
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

    DeliveryPolicy savedPolicy;

    @BeforeEach
    void setUp() {
        DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
                .deliveryPolicyFee(5000)
                .deliveryPolicyCondition(10000)
                .deliveryPolicyState(true)
                .build();

        savedPolicy = deliveryPolicyRepository.save(deliveryPolicy);
    }

    @Test
    @DisplayName("배송비 정책 저장 테스트")
    void testSaveDeliveryPolicy() {

        DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
                .deliveryPolicyFee(5000)
                .deliveryPolicyCondition(10000)
                .deliveryPolicyState(true)
                .build();

        DeliveryPolicy savedDeliveryPolicy = deliveryPolicyRepository.save(deliveryPolicy);

        assertTrue(deliveryPolicyRepository.existsById(savedDeliveryPolicy.getDeliveryPolicyId()));
    }

    @Test
    @DisplayName("배송비 정책 사용 여부 테스트")
    void testFindByState() {
        assertTrue(deliveryPolicyRepository.findByState(true).isPresent());
    }

    @Test
    @DisplayName("배송비 정책 전체 조회 - Querydsl")
    void testQuerydslGetDeliveryPolicies() {
        List<DeliveryPolicyResponseDto> deliveryPolicies = deliveryPolicyRepository.getDeliveryPolicies();

        assertAll(
                () -> assertEquals(1, deliveryPolicies.size()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyId(), deliveryPolicies.get(0).getDeliveryPolicyId()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyFee(), deliveryPolicies.get(0).getDeliveryPolicyFee()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyCondition(),
                        deliveryPolicies.get(0).getDeliveryPolicyCondition()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyState(),
                        deliveryPolicies.get(0).getDeliveryPolicyState())
        );
    }

    @Test
    @DisplayName("배송비 정책 ID 조회 - Querydsl")
    void testQuerydslGetDeliveryPolicyById() {
        Optional<DeliveryPolicyResponseDto> savedDto =
                deliveryPolicyRepository.getDeliveryPolicyById(savedPolicy.getDeliveryPolicyId());

        assertAll(
                () -> assertTrue(savedDto.isPresent()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyId(), savedDto.get().getDeliveryPolicyId()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyFee(), savedDto.get().getDeliveryPolicyFee()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyCondition(), savedDto.get().getDeliveryPolicyCondition()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyState(), savedDto.get().getDeliveryPolicyState())
        );
    }

    @Test
    @DisplayName("배송비 정책 사용 여부 조회 - Querydsl")
    void testQuerydslGetActiveDeliveryPolicy() {
        Optional<DeliveryPolicyResponseDto> savedDto = deliveryPolicyRepository.getActiveDeliveryPolicy();

        assertAll(
                () -> assertTrue(savedDto.isPresent()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyId(), savedDto.get().getDeliveryPolicyId()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyFee(), savedDto.get().getDeliveryPolicyFee()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyCondition(), savedDto.get().getDeliveryPolicyCondition()),
                () -> assertEquals(savedPolicy.getDeliveryPolicyState(), savedDto.get().getDeliveryPolicyState())
        );
    }
}