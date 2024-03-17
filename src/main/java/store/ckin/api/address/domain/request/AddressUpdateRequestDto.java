package store.ckin.api.address.domain.request;

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
    private Long addressId;

    private String base;

    private String detail;

    private String alias;

    private Boolean isDefault;
}
