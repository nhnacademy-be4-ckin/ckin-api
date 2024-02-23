package store.ckin.api.packaging.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.dto.request.PackagingUpdateRequestDto;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.entity.Packaging;
import store.ckin.api.packaging.exception.PackagingAlreadyExistsException;
import store.ckin.api.packaging.exception.PackagingNotFoundException;
import store.ckin.api.packaging.repository.PackagingRepository;

/**
 * 포장 정책 서비스 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@ExtendWith(MockitoExtension.class)
class PackagingServiceImplTest {

    @InjectMocks
    PackagingServiceImpl packagingService;

    @Mock
    PackagingRepository packagingRepository;

    @Test
    @DisplayName("포장 정책 생성 - 성공")
    void testCreatePackaging_Success() {
        PackagingCreateRequestDto requestDto = new PackagingCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingType", "생일선물");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", 5000);

        packagingService.createPackagingPolicy(requestDto);

        verify(packagingRepository, times(1)).existsByType(anyString());
        verify(packagingRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("포장 정책 실패 - 존재하는 포장지 종류")
    void testCreatePackaging_AlreadyExists() {
        PackagingCreateRequestDto requestDto = new PackagingCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingType", "생일선물");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", 5000);

        given(packagingRepository.existsByType(anyString()))
                .willReturn(true);

        assertThrows(PackagingAlreadyExistsException.class,
                () -> packagingService.createPackagingPolicy(requestDto));

        verify(packagingRepository, times(1)).existsByType(anyString());
        verify(packagingRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("포장 정책 개별 조회 - 성공")
    void testGetPackagingPolicy_Success() {

        PackagingResponseDto packaging = PackagingResponseDto.builder()
                .packagingId(1L)
                .packagingType("생일선물")
                .packagingPrice(5000)
                .build();

        given(packagingRepository.getPackagingById(anyLong()))
                .willReturn(Optional.of(packaging));

        PackagingResponseDto packagingPolicy = packagingService.getPackagingPolicy(1L);

        assertAll(
                () -> assertEquals(packaging.getPackagingId(), packagingPolicy.getPackagingId()),
                () -> assertEquals(packaging.getPackagingType(), packagingPolicy.getPackagingType()),
                () -> assertEquals(packaging.getPackagingPrice(), packagingPolicy.getPackagingPrice())
        );

        verify(packagingRepository, times(1)).getPackagingById(anyLong());
    }

    @Test
    @DisplayName("포장 정책 개별 조회 - 실패 (Not Found)")
    void testGetPackagingPolicy_Not_Found() {

        given(packagingRepository.getPackagingById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(PackagingNotFoundException.class, () -> packagingService.getPackagingPolicy(1L));

        verify(packagingRepository, times(1)).getPackagingById(anyLong());
    }

    @Test
    @DisplayName("포장 정책 모두 조회")
    void testGetPackagingPolicies() {

        List<PackagingResponseDto> packagingList = List.of(
                new PackagingResponseDto(1L, "꽃 생일선물 포장", 5000),
                new PackagingResponseDto(2L, "가죽 생일선물 포장", 10000)
        );

        given(packagingRepository.getAllPackaging())
                .willReturn(packagingList);


        List<PackagingResponseDto> packagingPolicies = packagingService.getPackagingPolicies();

        assertAll(
                () -> assertEquals(packagingList.get(0).getPackagingId(), packagingPolicies.get(0).getPackagingId()),
                () -> assertEquals(packagingList.get(0).getPackagingType(), packagingPolicies.get(0).getPackagingType()),
                () -> assertEquals(packagingList.get(0).getPackagingPrice(), packagingPolicies.get(0).getPackagingPrice()),
                () -> assertEquals(packagingList.get(1).getPackagingId(), packagingPolicies.get(1).getPackagingId()),
                () -> assertEquals(packagingList.get(1).getPackagingType(), packagingPolicies.get(1).getPackagingType()),
                () -> assertEquals(packagingList.get(1).getPackagingPrice(), packagingPolicies.get(1).getPackagingPrice())
        );

        verify(packagingRepository, times(1)).getAllPackaging();

    }

    @Test
    @DisplayName("포장 정책 수정 - 성공")
    void testUpdatePackagingPolicy_Success() {

        Packaging packaging = Packaging.builder()
                .packagingId(1L)
                .packagingType("생일선물 포장")
                .packagingPrice(5000)
                .build();

        given(packagingRepository.findById(anyLong()))
                .willReturn(Optional.of(packaging));

        PackagingUpdateRequestDto requestDto = new PackagingUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingId", 1L);
        ReflectionTestUtils.setField(requestDto, "packagingType", "그냥 포장");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", 1000);

        packagingService.updatePackagingPolicy(requestDto);

        assertEquals("그냥 포장", packaging.getPackagingType());
        assertEquals(1000, packaging.getPackagingPrice());

        verify(packagingRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("포장 정색 수정 - 실패 (Not Found)")
    void testUpdatePackagingPolicy_Not_Found() {

        PackagingUpdateRequestDto requestDto = new PackagingUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingId", 1L);
        ReflectionTestUtils.setField(requestDto, "packagingType", "그냥 포장");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", 1000);


        given(packagingRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(PackagingNotFoundException.class, () -> packagingService.updatePackagingPolicy(requestDto));
    }

    @Test
    @DisplayName("포장 정책 삭제 - 성공")
    void testDeletePackagingPolicy_Success() {
        given(packagingRepository.existsById(anyLong()))
                .willReturn(true);

        packagingService.deletePackagingPolicy(anyLong());

        verify(packagingRepository, times(1)).existsById(anyLong());
        verify(packagingRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("포장 정책 삭제 - 실패 (Not Found)")
    void testDeletePackagingPolicy_Not_Found() {

        given(packagingRepository.existsById(anyLong()))
                .willReturn(false);

        assertThrows(PackagingNotFoundException.class,
                () -> packagingService.deletePackagingPolicy(1L));
    }
}