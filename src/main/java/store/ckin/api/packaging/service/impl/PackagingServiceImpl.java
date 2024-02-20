package store.ckin.api.packaging.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.dto.request.PackagingUpdateRequestDto;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.entity.Packaging;
import store.ckin.api.packaging.exception.PackagingNotFoundException;
import store.ckin.api.packaging.repository.PackagingRepository;
import store.ckin.api.packaging.service.PackagingService;
import store.ckin.api.pointpolicy.exception.PointPolicyNotFoundException;

/**
 * 포장 정책 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@Service
@RequiredArgsConstructor
public class PackagingServiceImpl implements PackagingService {

    private final PackagingRepository packagingRepository;

    /**
     * {@inheritDoc}
     *
     * @param requestDto 포장 정책 생성 요청 DTO
     */
    @Transactional
    @Override
    public void createPackagingPolicy(PackagingCreateRequestDto requestDto) {
        Packaging packaging = Packaging.builder()
                .packagingType(requestDto.getPackagingType())
                .packagingPrice(requestDto.getPackagingPrice())
                .build();

        packagingRepository.save(packaging);
    }

    /**
     * {@inheritDoc}
     *
     * @param id 조회할 포장 정책 ID
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public PackagingResponseDto getPackagingPolicy(Long id) {

        return packagingRepository.findById(id)
                .map(PackagingResponseDto::toDto)
                .orElseThrow(() -> new PointPolicyNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     *
     * @return 포장 정책 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<PackagingResponseDto> getPackagingPolicies() {
        return packagingRepository.findAll()
                .stream()
                .map(PackagingResponseDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @param requestDto 포장 정책 수정 요청 DTO
     */
    @Transactional
    @Override
    public void updatePackagingPolicy(PackagingUpdateRequestDto requestDto) {
        Packaging packaging = packagingRepository.findById(requestDto.getPackagingId())
                .orElseThrow(() -> new PackagingNotFoundException(requestDto.getPackagingId()));

        packaging.update(requestDto);
    }

    /**
     * {@inheritDoc}
     *
     * @param id 삭제할 포장 정책 ID
     */
    @Transactional
    @Override
    public void deletePackagingPolicy(Long id) {
        if (!packagingRepository.existsById(id)) {
            throw new PackagingNotFoundException(id);
        }

        packagingRepository.deleteById(id);
    }
}
