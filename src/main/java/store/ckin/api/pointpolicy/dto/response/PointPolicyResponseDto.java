package store.ckin.api.pointpolicy.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 13.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointPolicyResponseDto {

    private Long pointPolicyId;

    private String pointPolicyName;

    private Integer pointPolicyReserve;

    @Builder
    public PointPolicyResponseDto(Long pointPolicyId, String pointPolicyName, Integer pointPolicyReserve) {
        this.pointPolicyId = pointPolicyId;
        this.pointPolicyName = pointPolicyName;
        this.pointPolicyReserve = pointPolicyReserve;
    }
}
