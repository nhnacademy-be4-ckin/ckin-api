package store.ckin.api.pointpolicy.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 정책 수정 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */

@Getter
@NoArgsConstructor
public class PointPolicyUpdateRequestDto {

    @Size(min = 1, max = 15, message = "포인트 정책 이름은 1자 이상 15자 이하로 입력해주세요.")
    @NotBlank(message = "포인트 정책 이름을 입력해주세요.")
    private String pointPolicyName;

    @NotNull(message = "포인트 적립 금액을 입력해주세요.")
    @PositiveOrZero(message = "포인트 적립 금액은 0원 이상으로 입력해주세요.")
    private Integer pointPolicyReserve;
}
