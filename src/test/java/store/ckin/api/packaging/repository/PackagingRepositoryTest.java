package store.ckin.api.packaging.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
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

    Packaging savedPackaging;

    @BeforeEach
    void setUp() {
        Packaging packaging = Packaging.builder()
                .packagingId(null)
                .packagingType("생일선물")
                .packagingPrice(5000)
                .build();

        savedPackaging = packagingRepository.save(packaging);
    }

    @Test
    @DisplayName("포장 정책 저장 테스트")
    void testSavePackagingPolicy() {

        Packaging packaging = Packaging.builder()
                .packagingId(null)
                .packagingType("테스트")
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
        assertTrue(packagingRepository.existsByType(savedPackaging.getPackagingType()));
    }

    @Test
    @DisplayName("포장 정책 ID로 조회")
    void testGetPackagingById() {

        Optional<PackagingResponseDto> packaging =
                packagingRepository.getPackagingById(savedPackaging.getPackagingId());

        assertTrue(packaging.isPresent());
        assertEquals(savedPackaging.getPackagingType(), packaging.get().getPackagingType());
        assertEquals(savedPackaging.getPackagingPrice(), packaging.get().getPackagingPrice());
    }

    @Test
    @DisplayName("모든 포장 정책 조회")
    void testGetAllPackaging() {
        List<PackagingResponseDto> allPackaging = packagingRepository.getAllPackaging();

        assertEquals(1, allPackaging.size());
        assertEquals(savedPackaging.getPackagingId(), allPackaging.get(0).getPackagingId());
        assertEquals(savedPackaging.getPackagingType(), allPackaging.get(0).getPackagingType());
        assertEquals(savedPackaging.getPackagingPrice(), allPackaging.get(0).getPackagingPrice());
    }
}