package store.ckin.api.packaging.service;

import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;

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
}
