package store.ckin.api.pointpolicy.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.pointpolicy.dto.request.CreatePointPolicyRequestDto;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.exception.PointPolicyAlreadyExistsException;
import store.ckin.api.pointpolicy.repository.PointPolicyRepository;
import store.ckin.api.pointpolicy.service.PointPolicyService;

/**
 * 포인트 정책을 관리하는 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */

@Service
@RequiredArgsConstructor
public class PointPolicyServiceImpl implements PointPolicyService {

    private final PointPolicyRepository pointPolicyRepository;

    @Transactional
    @Override
    public void createPointPolicy(CreatePointPolicyRequestDto request) {

        if (pointPolicyRepository.existsById(request.getPointPolicyId())) {
            throw new PointPolicyAlreadyExistsException(request.getPointPolicyId(), request.getPointPolicyName());
        }

        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(request.getPointPolicyId())
                .pointPolicyName(request.getPointPolicyName())
                .pointPolicyReserve(request.getPointPolicyReserve())
                .build();

        pointPolicyRepository.save(pointPolicy);
    }

    @Override
    public List<PointPolicyResponseDto> getPointPolicies() {
        return pointPolicyRepository.findAll()
                .stream()
                .map(PointPolicyResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
