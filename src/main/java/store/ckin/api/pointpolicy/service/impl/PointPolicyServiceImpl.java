package store.ckin.api.pointpolicy.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.pointpolicy.dto.request.PointPolicyCreateRequestDto;
import store.ckin.api.pointpolicy.dto.request.PointPolicyUpdateRequestDto;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.exception.PointPolicyAlreadyExistsException;
import store.ckin.api.pointpolicy.exception.PointPolicyNotFoundException;
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


    /**
     * {@inheritDoc}
     *
     * @param id 조회할 포인트 정책 ID
     * @return 조회된 포인트 정책 응답 DTO
     */
    @Transactional(readOnly = true)
    @Override
    public PointPolicyResponseDto getPointPolicy(Long id) {
        return pointPolicyRepository.getPointPolicyById(id)
                .orElseThrow(() -> new PointPolicyNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     *
     * @return 모든 포인트 정책 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<PointPolicyResponseDto> getPointPolicies() {
        return pointPolicyRepository.getPointPolicies();
    }

    /**
     * {@inheritDoc}
     *
     * @param request 포인트 정책 생성 요청 DTO
     */
    @Transactional
    @Override
    public void createPointPolicy(PointPolicyCreateRequestDto request) {

        if (pointPolicyRepository.existsPointPolicy(request.getPointPolicyId(), request.getPointPolicyName())) {
            throw new PointPolicyAlreadyExistsException(request.getPointPolicyId(), request.getPointPolicyName());
        }

        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(request.getPointPolicyId())
                .pointPolicyName(request.getPointPolicyName())
                .pointPolicyReserve(request.getPointPolicyReserve())
                .build();

        pointPolicyRepository.save(pointPolicy);
    }

    /**
     * {@inheritDoc}
     *
     * @param updatePointPolicy 수정할 포인트 정책 요청 DTO
     */
    @Transactional
    @Override
    public void updatePointPolicy(Long id, PointPolicyUpdateRequestDto updatePointPolicy) {
        PointPolicy pointPolicy = pointPolicyRepository.findById(id)
                .orElseThrow(() -> new PointPolicyNotFoundException(id));

        pointPolicy.update(updatePointPolicy.getPointPolicyName(),
                updatePointPolicy.getPointPolicyReserve());
    }

    /**
     * {@inheritDoc}
     *
     * @param id 삭제할 포인트 정책 ID
     */
    @Transactional
    @Override
    public void deletePointPolicy(Long id) {
        if (!pointPolicyRepository.existsById(id)) {
            throw new PointPolicyNotFoundException(id);
        }

        pointPolicyRepository.deleteById(id);
    }
}
