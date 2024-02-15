package store.ckin.api.deliverypolicy.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyCreateRequestDto;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.entity.DeliveryPolicy;
import store.ckin.api.deliverypolicy.repository.DeliveryPolicyRepository;
import store.ckin.api.deliverypolicy.service.DeliveryPolicyService;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@Service
@RequiredArgsConstructor
public class DeliveryPolicyServiceImpl implements DeliveryPolicyService {

    private final DeliveryPolicyRepository deliveryPolicyRepository;


    @Override
    public List<DeliveryPolicyResponseDto> getDeliveryPolicies() {
        return deliveryPolicyRepository.findAll()
                .stream()
                .map(DeliveryPolicyResponseDto::toDto)
                .collect(Collectors.toList());
    }

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
