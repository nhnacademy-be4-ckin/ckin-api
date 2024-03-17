package store.ckin.api.address.domain.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주소 수정을 요청하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
@Getter
@NoArgsConstructor
public class AddressUpdateRequestDto {
    @NotBlank
    private String base;

    @NotBlank
    private String detail;

    @NotBlank
    private String alias;
}
