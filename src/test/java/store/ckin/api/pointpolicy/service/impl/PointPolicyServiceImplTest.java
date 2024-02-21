package store.ckin.api.pointpolicy.service.impl;

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
import store.ckin.api.pointpolicy.dto.request.PointPolicyCreateRequestDto;
import store.ckin.api.pointpolicy.dto.request.PointPolicyUpdateRequestDto;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.exception.PointPolicyAlreadyExistsException;
import store.ckin.api.pointpolicy.exception.PointPolicyNotFoundException;
import store.ckin.api.pointpolicy.repository.PointPolicyRepository;

/**
 * 포인트 정책 서비스 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@ExtendWith(MockitoExtension.class)
class PointPolicyServiceImplTest {

    @InjectMocks
    PointPolicyServiceImpl pointPolicyService;

    @Mock
    PointPolicyRepository pointPolicyRepository;

    @Test
    @DisplayName("포인트 정책 개별 조회 - 성공")
    void testGetPointPolicy_Success() {

        // given
        PointPolicyResponseDto pointPolicy =
                PointPolicyResponseDto.builder()
                        .pointPolicyId(1L)
                        .pointPolicyName("회원가입")
                        .pointPolicyReserve(5000)
                        .build();

        given(pointPolicyRepository.getPointPolicyById(anyLong()))
                .willReturn(Optional.of(pointPolicy));

        // when
        PointPolicyResponseDto actual = pointPolicyService.getPointPolicy(1L);

        // then
        assertAll(
                () -> assertEquals(1L, actual.getPointPolicyId()),
                () -> assertEquals("회원가입", actual.getPointPolicyName()),
                () -> assertEquals(5000, actual.getPointPolicyReserve())
        );

        verify(pointPolicyRepository, times(1)).getPointPolicyById(anyLong());
    }

    @Test
    @DisplayName("포인트 정책 개별 조회 - 실패 (NotFound)")
    void testGetPointPolicy_Fail_NotFound() {

        // given
        given(pointPolicyRepository.getPointPolicyById(anyLong()))
                .willReturn(Optional.empty());

        // when

        // then
        assertThrows(PointPolicyNotFoundException.class, () -> pointPolicyService.getPointPolicy(1L));
    }

    @Test
    @DisplayName("포인트 정책 조회 - 모든 포인트 정책 조회")
    void testGetPointPolicies() {

        // given
        List<PointPolicyResponseDto> pointPolicies = List.of(
                new PointPolicyResponseDto(1L, "회원가입", 5000),
                new PointPolicyResponseDto(2L, "리뷰작성", 200),
                new PointPolicyResponseDto(3L, "사진 포함 리뷰 작성", 300)
        );

        given(pointPolicyRepository.getPointPolicies())
                .willReturn(pointPolicies);

        // when
        List<PointPolicyResponseDto> actual = pointPolicyService.getPointPolicies();

        // then
        assertEquals(3, actual.size());
        assertEquals(1L, actual.get(0).getPointPolicyId());
        assertEquals("리뷰작성", actual.get(1).getPointPolicyName());
        assertEquals(300, actual.get(2).getPointPolicyReserve());

        verify(pointPolicyRepository, times(1)).getPointPolicies();
    }

    @Test
    @DisplayName("포인트 정책 생성 - 성공")
    void testCreatePointPolicy_Success() {

        // given
        PointPolicyCreateRequestDto createPointPolicy = new PointPolicyCreateRequestDto();
        ReflectionTestUtils.setField(createPointPolicy, "pointPolicyId", 1L);
        ReflectionTestUtils.setField(createPointPolicy, "pointPolicyName", "회원가입");
        ReflectionTestUtils.setField(createPointPolicy, "pointPolicyReserve", 5000);


        given(pointPolicyRepository.existsPointPolicy(anyLong(), anyString())).willReturn(false);

        // when
        pointPolicyService.createPointPolicy(createPointPolicy);

        // then
        verify(pointPolicyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("포인트 정책 생성 - 실패(AlreadyExists)")
    void testCreatePointPolicy_Fail_AlreadyExists() {

        // given
        PointPolicyCreateRequestDto createPointPolicy = new PointPolicyCreateRequestDto();
        ReflectionTestUtils.setField(createPointPolicy, "pointPolicyId", 1L);
        ReflectionTestUtils.setField(createPointPolicy, "pointPolicyName", "회원가입");
        ReflectionTestUtils.setField(createPointPolicy, "pointPolicyReserve", 5000);

        given(pointPolicyRepository.existsPointPolicy(anyLong(), anyString())).willReturn(true);

        // when

        // then
        assertThrows(PointPolicyAlreadyExistsException.class,
                () -> pointPolicyService.createPointPolicy(createPointPolicy));
    }

    @Test
    @DisplayName("포인트 정책 수정 - 성공")
    void testUpdatePointPolicy_Success() {

        // given
        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(1L)
                .pointPolicyName("회원가입")
                .pointPolicyReserve(5000)
                .build();

        given(pointPolicyRepository.findById(anyLong()))
                .willReturn(Optional.of(pointPolicy));

        // when
        PointPolicyUpdateRequestDto updateRequestDto = new PointPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(updateRequestDto, "pointPolicyName", "리뷰작성");
        ReflectionTestUtils.setField(updateRequestDto, "pointPolicyReserve", 200);
        pointPolicyService.updatePointPolicy(pointPolicy.getPointPolicyId(), updateRequestDto);

        // then
        assertEquals("리뷰작성", pointPolicy.getPointPolicyName());
        assertEquals(200, pointPolicy.getPointPolicyReserve());
    }

    @Test
    @DisplayName("포인트 정책 수정 - 실패(NotFound)")
    void testUpdatePointPolicy_Fail_NotFound() {

        // given
        PointPolicyUpdateRequestDto updateRequestDto = new PointPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(updateRequestDto, "pointPolicyId", 1L);
        ReflectionTestUtils.setField(updateRequestDto, "pointPolicyName", "리뷰작성");
        ReflectionTestUtils.setField(updateRequestDto, "pointPolicyReserve", 200);

        given(pointPolicyRepository.findById(anyLong())).willReturn(Optional.empty());

        // when

        // then
        assertThrows(PointPolicyNotFoundException.class, () -> pointPolicyService.updatePointPolicy(1L, updateRequestDto));
    }

    @Test
    @DisplayName("포인트 정책 삭제 - 성공")
    void testDeletePointPolicy_Success() {

        // given
        given(pointPolicyRepository.existsById(anyLong()))
                .willReturn(true);

        // when
        pointPolicyService.deletePointPolicy(1L);

        // then
        verify(pointPolicyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("포인트 정책 삭제 - 실패(NotFound)")
    void testDeletePointPolicy_Fail_NotFound() {

        // given
        given(pointPolicyRepository.existsById(anyLong()))
                .willReturn(false);

        // when

        // then
        assertThrows(PointPolicyNotFoundException.class, () -> pointPolicyService.deletePointPolicy(1L));
    }
}