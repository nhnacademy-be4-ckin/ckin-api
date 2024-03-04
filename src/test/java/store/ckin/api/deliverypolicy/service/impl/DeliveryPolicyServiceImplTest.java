package store.ckin.api.deliverypolicy.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
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
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyCreateRequestDto;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyUpdateRequestDto;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;
import store.ckin.api.deliverypolicy.exception.DeliveryPolicyNotActiveException;
import store.ckin.api.deliverypolicy.exception.DeliveryPolicyNotFoundException;
import store.ckin.api.deliverypolicy.repository.DeliveryPolicyRepository;

/**
 * 배송비 정책 서비스 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@ExtendWith(MockitoExtension.class)
class DeliveryPolicyServiceImplTest {

    @InjectMocks
    DeliveryPolicyServiceImpl deliveryPolicyService;

    @Mock
    DeliveryPolicyRepository deliveryPolicyRepository;

    @Test
    @DisplayName("배송비 정책 개별 조회 - 성공")
    void testGetPointPolicy_Success() {

        // given
        DeliveryPolicyResponseDto deliveryPolicy =
                DeliveryPolicyResponseDto.builder()
                        .deliveryPolicyId(1L)
                        .deliveryPolicyFee(5000)
                        .deliveryPolicyCondition(10000)
                        .deliveryPolicyState(true)
                        .build();

        given(deliveryPolicyRepository.getDeliveryPolicyById(anyLong()))
                .willReturn(Optional.of(deliveryPolicy));

        // when
        DeliveryPolicyResponseDto actual = deliveryPolicyService.getDeliveryPolicy(1L);

        // then
        assertAll(
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyId(), actual.getDeliveryPolicyId()),
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyFee(), actual.getDeliveryPolicyFee()),
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyCondition(), actual.getDeliveryPolicyCondition()),
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyState(), actual.getDeliveryPolicyState())
        );

        verify(deliveryPolicyRepository, times(1)).getDeliveryPolicyById(anyLong());
    }

    @Test
    @DisplayName("배송비 정책 개별 조회 - 실패 (Not Found)")
    void testGetPointPolicy_Fail_NotFound() {

        given(deliveryPolicyRepository.getDeliveryPolicyById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(DeliveryPolicyNotFoundException.class,
                () -> deliveryPolicyService.getDeliveryPolicy(1L));
    }

    @Test
    @DisplayName("배송비 정책 리스트 조회")
    void testGetPointPolicies() {
        List<DeliveryPolicyResponseDto> dtoList =
                List.of(new DeliveryPolicyResponseDto(1L, 5000, 10000, true),
                        new DeliveryPolicyResponseDto(2L, 1000, 3000, false));

        given(deliveryPolicyRepository.getDeliveryPolicies()).willReturn(dtoList);

        List<DeliveryPolicyResponseDto> deliveryPolicies = deliveryPolicyService.getDeliveryPolicies();

        assertEquals(2, deliveryPolicies.size());
        assertAll(
                () -> assertEquals(dtoList.get(0).getDeliveryPolicyId(), deliveryPolicies.get(0).getDeliveryPolicyId()),
                () -> assertEquals(dtoList.get(1).getDeliveryPolicyId(), deliveryPolicies.get(1).getDeliveryPolicyId()),
                () -> assertEquals(dtoList.get(0).getDeliveryPolicyFee(),
                        deliveryPolicies.get(0).getDeliveryPolicyFee()),
                () -> assertEquals(dtoList.get(1).getDeliveryPolicyFee(),
                        deliveryPolicies.get(1).getDeliveryPolicyFee()),
                () -> assertEquals(dtoList.get(0).getDeliveryPolicyCondition(),
                        deliveryPolicies.get(0).getDeliveryPolicyCondition()),
                () -> assertEquals(dtoList.get(1).getDeliveryPolicyCondition(),
                        deliveryPolicies.get(1).getDeliveryPolicyCondition()),
                () -> assertTrue(deliveryPolicies.get(0).getDeliveryPolicyState()),
                () -> assertFalse(deliveryPolicies.get(1).getDeliveryPolicyState())
        );
    }

    @Test
    @DisplayName("배송비 정책 수정 - 성공")
    void testUpdateDeliveryPolicy_Success() {

        DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
                .deliveryPolicyId(1L)
                .deliveryPolicyFee(5000)
                .deliveryPolicyCondition(10000)
                .deliveryPolicyState(true)
                .build();

        given(deliveryPolicyRepository.findById(anyLong()))
                .willReturn(Optional.of(deliveryPolicy));

        DeliveryPolicyUpdateRequestDto updateDto = new DeliveryPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyFee", 300);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyCondition", 500);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyState", false);


        deliveryPolicyService.updateDeliveryPolicy(1L, updateDto);

        assertAll(
                () -> assertEquals(updateDto.getDeliveryPolicyFee(), deliveryPolicy.getDeliveryPolicyFee()),
                () -> assertEquals(updateDto.getDeliveryPolicyCondition(), deliveryPolicy.getDeliveryPolicyCondition()),
                () -> assertEquals(updateDto.getDeliveryPolicyState(), deliveryPolicy.getDeliveryPolicyState())
        );
    }

    @Test
    @DisplayName("배송비 정책 수정 - 실패")
    void testUpdateDeliveryPolicy_Fail() {

        given(deliveryPolicyRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        DeliveryPolicyUpdateRequestDto updateDto = new DeliveryPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyFee", 300);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyCondition", 500);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyState", false);

        assertThrows(DeliveryPolicyNotFoundException.class,
                () -> deliveryPolicyService.updateDeliveryPolicy(1L, updateDto));
    }

    @Test
    @DisplayName("배송비 정책 생성 - 배송비 정책 사용 여부가 true인 경우")
    void testCreateDeliveryPolicy_TruePolicy() {

        DeliveryPolicyCreateRequestDto deliveryPolicy = new DeliveryPolicyCreateRequestDto();
        ReflectionTestUtils.setField(deliveryPolicy, "deliveryPolicyFee", 5000);
        ReflectionTestUtils.setField(deliveryPolicy, "deliveryPolicyCondition", 10000);
        ReflectionTestUtils.setField(deliveryPolicy, "deliveryPolicyState", true);

        deliveryPolicyService.createDeliveryPolicy(deliveryPolicy);

        verify(deliveryPolicyRepository, times(1)).findByState(anyBoolean());
        verify(deliveryPolicyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("배송비 정책 생성 - 배송비 정책 사용 여부가 false인 경우")
    void testCreateDeliveryPolicy_FalsePolicy() {

        DeliveryPolicyCreateRequestDto deliveryPolicy = new DeliveryPolicyCreateRequestDto();
        ReflectionTestUtils.setField(deliveryPolicy, "deliveryPolicyFee", 5000);
        ReflectionTestUtils.setField(deliveryPolicy, "deliveryPolicyCondition", 10000);
        ReflectionTestUtils.setField(deliveryPolicy, "deliveryPolicyState", false);

        deliveryPolicyService.createDeliveryPolicy(deliveryPolicy);

        verify(deliveryPolicyRepository, times(0)).findByState(anyBoolean());
        verify(deliveryPolicyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("배송비 정책 조회 - 활성화된 배송비 정책 조회")
    void testGetActiveDeliveryPolicy() {

        DeliveryPolicyResponseDto deliveryPolicy =
                DeliveryPolicyResponseDto.builder()
                        .deliveryPolicyId(1L)
                        .deliveryPolicyFee(5000)
                        .deliveryPolicyCondition(10000)
                        .deliveryPolicyState(true)
                        .build();

        given(deliveryPolicyRepository.getActiveDeliveryPolicy())
                .willReturn(Optional.of(deliveryPolicy));

        DeliveryPolicyResponseDto actual = deliveryPolicyService.getActiveDeliveryPolicy();

        assertAll(
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyId(), actual.getDeliveryPolicyId()),
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyFee(), actual.getDeliveryPolicyFee()),
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyCondition(), actual.getDeliveryPolicyCondition()),
                () -> assertEquals(deliveryPolicy.getDeliveryPolicyState(), actual.getDeliveryPolicyState())
        );

        verify(deliveryPolicyRepository, times(1)).getActiveDeliveryPolicy();
    }

    @Test
    @DisplayName("배송비 정책 조회 - 활성화된 배송비 정책이 없는 경우 (NotActiveException)")
    void testGetActiveDeliveryPolicy_NotActive() {

        given(deliveryPolicyRepository.getActiveDeliveryPolicy())
                .willReturn(Optional.empty());

        assertThrows(DeliveryPolicyNotActiveException.class,
                () -> deliveryPolicyService.getActiveDeliveryPolicy());

        verify(deliveryPolicyRepository, times(1)).getActiveDeliveryPolicy();
    }
}