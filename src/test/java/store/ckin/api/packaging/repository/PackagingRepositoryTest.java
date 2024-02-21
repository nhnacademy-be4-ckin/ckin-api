package store.ckin.api.packaging.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.packaging.entity.Packaging;

/**
 * 포장 정책 레포지토리 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@DataJpaTest
class PackagingRepositoryTest {

    @Autowired
    PackagingRepository packagingRepository;

    @Test
    @DisplayName("포장 정책 저장 테스트")
    void testSavePackagingPolicy() {

        Packaging packaging = Packaging.builder()
                .packagingId(null)
                .packagingType("생일선물")
                .packagingPrice(5000)
                .build();

        Packaging savedPackaging = packagingRepository.save(packaging);

        assertAll(
                () -> assertNotNull(savedPackaging.getPackagingId()),
                () -> assertEquals(packaging.getPackagingType(), savedPackaging.getPackagingType()),
                () -> assertEquals(packaging.getPackagingPrice(), savedPackaging.getPackagingPrice())
        );
    }

    @Test
    @DisplayName("포장 정책 존재하는 포장지 타입 조회 테스트")
    void testExistsByType() {
        Packaging packaging = Packaging.builder()
                .packagingId(null)
                .packagingType("생일선물")
                .packagingPrice(5000)
                .build();

        packagingRepository.save(packaging);

        assertTrue(packagingRepository.existsByType("생일선물"));
    }

}