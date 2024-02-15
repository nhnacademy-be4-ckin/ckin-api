package store.ckin.api.deliverypolicy.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyCreateRequestDto;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;
import store.ckin.api.deliverypolicy.exception.DeliveryPolicyNotFoundException;
import store.ckin.api.deliverypolicy.repository.DeliveryPolicyRepository;
import store.ckin.api.deliverypolicy.service.DeliveryPolicyService;

/**
 * 배송비 정책을 관리하는 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Service
@RequiredArgsConstructor
public class DeliveryPolicyServiceImpl implements DeliveryPolicyService {

    private final DeliveryPolicyRepository deliveryPolicyRepository;

    /**
     * {@inheritDoc}
     *
     * @return 배송비 정책 응답 DTO 리스트
     */
    @Override
    public List<DeliveryPolicyResponseDto> getDeliveryPolicies() {
        return deliveryPolicyRepository.findAll()
                .stream()
                .map(DeliveryPolicyResponseDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @param id 조회할 배송비 정책 ID
     * @return 조회된 배송비 정책 응답 DTO
     */
    @Override
    public DeliveryPolicyResponseDto getDeliveryPolicy(Long id) {
        return deliveryPolicyRepository.findById(id)
                .map(DeliveryPolicyResponseDto::toDto)
                .orElseThrow(() -> new DeliveryPolicyNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param createDeliveryPolicy 생성할 배송비 정책 요청 DTO
     */
    @Override
    public void createDeliveryPolicy(DeliveryPolicyCreateRequestDto createDeliveryPolicy) {

        DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
                .deliveryPolicyFee(createDeliveryPolicy.getDeliveryPolicyFee())
                .deliveryPolicyCondition(createDeliveryPolicy.getDeliveryPolicyCondition())
                .deliveryPolicyState(createDeliveryPolicy.getDeliveryPolicyState())
                .build();

        deliveryPolicyRepository.save(deliveryPolicy);
    }

}
