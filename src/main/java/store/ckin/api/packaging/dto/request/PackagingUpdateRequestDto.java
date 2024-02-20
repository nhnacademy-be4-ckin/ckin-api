package store.ckin.api.packaging.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 포장 정책 수정 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@Getter
@AllArgsConstructor
public class PackagingUpdateRequestDto {

    @NotNull(message = "포장 정책가 없습니다.")
    private Long packagingId;

    @NotBlank(message = "포장지 종류를 입력해주세요.")
    @Size(min = 1, max = 25, message = "포장지 종류 이름은 1자 이상 25자 이하로 입력해주세요.")
    private String packagingType;

    @NotNull(message = "포장지 가격을 입력해주세요")
    @Positive(message = "포장지 가격은 0원보다 커야합니다.")
    private Integer packagingPrice;
}
