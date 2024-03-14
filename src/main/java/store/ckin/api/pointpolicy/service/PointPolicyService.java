package store.ckin.api.pointpolicy.service;

import java.util.List;

import store.ckin.api.pointpolicy.dto.request.PointPolicyCreateRequestDto;
import store.ckin.api.pointpolicy.dto.request.PointPolicyUpdateRequestDto;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;

/**
 * 포인트 정책을 관리하는 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */
public interface PointPolicyService {


    /**
     * 포인트 정책 개별 조회를 하는 메서드입니다.
     *
     * @param id 조회할 포인트 정책 ID
     * @return 조회된 포인트 정책 응답 DTO
     */
    PointPolicyResponseDto getPointPolicy(Long id);

    /**
     * 포인트 정책 리스트를 조회하는 메서드입니다.
     *
     * @return 포인트 정책 리스트
     */
    List<PointPolicyResponseDto> getPointPolicies();

    /**
     * 포인트 정책 생성을 위한 메서드입니다.
     *
     * @param request 포인트 정책 생성 요청 DTO
     */
    void createPointPolicy(PointPolicyCreateRequestDto request);

    /**
     * 포인트 정책을 수정하는 메서드입니다.
     *
     * @param id                수정할 포인트 정책 ID
     * @param updatePointPolicy 수정할 포인트 정책 요청 DTO
     */
    void updatePointPolicy(Long id, PointPolicyUpdateRequestDto updatePointPolicy);

    /**
     * 포인트 정책를 삭제하는 메서드입니다.
     *
     * @param id 삭제할 포인트 정책 ID
     */
    void deletePointPolicy(Long id);

}
