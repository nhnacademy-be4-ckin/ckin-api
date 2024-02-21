package store.ckin.api.pointpolicy.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.entity.PointPolicy;

/**
 * 포인트 정책 레포지토리 테스트 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@DataJpaTest
class PointPolicyRepositoryTest {

    @Autowired
    PointPolicyRepository pointPolicyRepository;

    PointPolicy savedPointPolicy;

    @BeforeEach
    void setUp() {
        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(1L)
                .pointPolicyName("포인트 정책1")
                .pointPolicyReserve(3000)
                .build();

        savedPointPolicy = pointPolicyRepository.save(pointPolicy);
    }

    @Test
    @DisplayName("포인트 정책 저장 테스트")
    void testSavePointPolicy() {

        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(100L)
                .pointPolicyName("새로운 정책")
                .pointPolicyReserve(1234)
                .build();

        PointPolicy actual = pointPolicyRepository.save(pointPolicy);

        assertAll(
                () -> assertEquals(pointPolicy.getPointPolicyId(), actual.getPointPolicyId()),
                () -> assertEquals(pointPolicy.getPointPolicyName(), actual.getPointPolicyName()),
                () -> assertEquals(pointPolicy.getPointPolicyReserve(), actual.getPointPolicyReserve())
        );
    }

    @Test
    @DisplayName("포인트 정책 ID 조회 테스트")
    void testGetPointPolicy() {

        Optional<PointPolicy> actual = pointPolicyRepository.findById(1L);

        assertTrue(actual.isPresent());

        PointPolicy pointPolicy = actual.get();
        assertAll(

                () -> assertEquals(1L, pointPolicy.getPointPolicyId()),
                () -> assertEquals("포인트 정책1", pointPolicy.getPointPolicyName()),
                () -> assertEquals(3000, pointPolicy.getPointPolicyReserve())
        );
    }

    @Test
    @DisplayName("포인트 정책 전체 조회 테스트")
    void testGetPointPolicies() {
        List<PointPolicy> pointPolicies = pointPolicyRepository.findAll();

        assertEquals(1, pointPolicies.size());
    }

    @Test
    @DisplayName("포인트 정책 삭제")
    void testDeletePointPolicy() {
        pointPolicyRepository.deleteById(1L);
        assertFalse(pointPolicyRepository.existsById(1L));
    }


    @Test
    @DisplayName("포인트 정책 유무 확인 - Querydsl")
    void testQuerydslExistsPointPolicy() {
        boolean actual = pointPolicyRepository.existsPointPolicy(1L, "포인트 정책1");
        assertTrue(actual);
    }

    @Test
    @DisplayName("포인트 정책 단건 조회 - Querydsl")
    void testQuerydslGetPointPolicyById() {
        Optional<PointPolicyResponseDto> responseDto = pointPolicyRepository.getPointPolicyById(1L);

        assertAll(
                () -> assertTrue(responseDto.isPresent()),
                () -> assertEquals(savedPointPolicy.getPointPolicyId(), responseDto.get().getPointPolicyId()),
                () -> assertEquals(savedPointPolicy.getPointPolicyName(), responseDto.get().getPointPolicyName()),
                () -> assertEquals(savedPointPolicy.getPointPolicyReserve(), responseDto.get().getPointPolicyReserve())
        );
    }

    @Test
    @DisplayName("포인트 정책 전체 조회 - Querydsl")
    void testQuerydslGetPointPolicies() {
        List<PointPolicyResponseDto> pointPolicies = pointPolicyRepository.getPointPolicies();

        assertAll(
                () -> assertEquals(1, pointPolicies.size()),
                () -> assertEquals(savedPointPolicy.getPointPolicyId(), pointPolicies.get(0).getPointPolicyId()),
                () -> assertEquals(savedPointPolicy.getPointPolicyName(), pointPolicies.get(0).getPointPolicyName()),
                () -> assertEquals(savedPointPolicy.getPointPolicyReserve(), pointPolicies.get(0).getPointPolicyReserve())
        );
    }
}