package store.ckin.api.pointpolicy.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;

/**
 * 포인트 정책 수정 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */

@Getter
public class PointPolicyUpdateRequestDto {

    @Positive(message = "포인트 정책 ID는 1 이상의 숫자로 입력해주세요.")
    @NotNull(message = "포인트 정책 ID를 입력해주세요.")
    private Long pointPolicyId;

    @Size(min = 1, max = 15, message = "포인트 정책 이름은 1자 이상 15자 이하로 입력해주세요.")
    @NotBlank(message = "포인트 정책 이름을 입력해주세요.")
    private String pointPolicyName;

    @NotNull(message = "포인트 적립 금액을 입력해주세요.")
    @PositiveOrZero(message = "포인트 적립 금액은 0원 이상으로 입력해주세요.")
    private Integer pointPolicyReserve;
}
