package store.ckin.api.packaging.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * 포장 정책 생성 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@Getter
public class PackagingCreateRequestDto {

    @NotBlank(message = "포장지 종류를 입력해주세요.")
    @Size(min = 1, max = 25, message = "포장지 정책 이름은 1자 이상 15자 이하로 입력해주세요.")
    private String packagingType;

    @NotNull
    @Positive(message = "포장지 가격은 0원보다 커야합니다.")
    private Integer packagingPrice;
}
