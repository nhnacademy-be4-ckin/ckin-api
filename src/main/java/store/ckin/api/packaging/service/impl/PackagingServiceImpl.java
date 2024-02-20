package store.ckin.api.packaging.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.entity.Packaging;
import store.ckin.api.packaging.repository.PackagingRepository;
import store.ckin.api.packaging.service.PackagingService;

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
     * @return 포장 정책 응답 DTO 리스트
     */
    @Override
    public List<PackagingResponseDto> getPackagingPolicies() {
        return packagingRepository.findAll()
                .stream().map(PackagingResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
