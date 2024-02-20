package store.ckin.api.packaging.service;

import java.util.List;
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;

/**
 * 포장 정책 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */
public interface PackagingService {

    /**
     * 포장 정책 생성 메서드입니다.
     *
     * @param requestDto 포장 정책 생성 요청 DTO
     */
    void createPackagingPolicy(PackagingCreateRequestDto requestDto);

    /**
     * 포장 정책 리스트 조회 메서드입니다.
     *
     * @return 포장 정책 응답 DTO 리스트
     */
    List<PackagingResponseDto> getPackagingPolicies();

    /**
     * 포장 정책 삭제 메서드입니다.
     *
     * @param id 삭제할 포장 정책 ID
     */
    void deletePackagingPolicy(Long id);
}
