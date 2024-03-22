package store.ckin.api.pointpolicy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 포인트 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 13.
 */

@Getter
@Builder
@AllArgsConstructor
public class PointPolicyResponseDto {

    private Long pointPolicyId;

    private String pointPolicyName;

    private Integer pointPolicyReserve;
}
