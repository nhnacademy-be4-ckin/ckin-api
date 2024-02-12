package store.ckin.api.pointpolicy.service;

import store.ckin.api.pointpolicy.dto.request.CreatePointPolicyRequestDto;

/**
 * 포인트 정책을 관리하는 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */
public interface PointPolicyService {

    /**
     * 포인트 정책 생성을 위한 메서드입니다.
     *
     * @param request 포인트 정책 생성 요청 DTO
     */
    void createPointPolicy(CreatePointPolicyRequestDto request);
}
