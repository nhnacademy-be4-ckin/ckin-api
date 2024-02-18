package store.ckin.api.pointpolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import store.ckin.api.pointpolicy.entity.PointPolicy;

/**
 * 포인트 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 13.
 */

@Builder
@Getter
@AllArgsConstructor
public class PointPolicyResponseDto {

    private Long pointPolicyId;

    private String pointPolicyName;

    private Integer pointPolicyReserve;

    public static PointPolicyResponseDto toDto(PointPolicy entity) {
        return PointPolicyResponseDto.builder()
                .pointPolicyId(entity.getPointPolicyId())
                .pointPolicyName(entity.getPointPolicyName())
                .pointPolicyReserve(entity.getPointPolicyReserve())
                .build();
    }
}
