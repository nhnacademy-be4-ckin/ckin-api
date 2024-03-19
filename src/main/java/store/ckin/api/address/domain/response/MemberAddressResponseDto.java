package store.ckin.api.address.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 특정 멤버의 주소 정보를 응답하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
@Getter
@AllArgsConstructor
public class MemberAddressResponseDto {
    private Long addressId;

    private String postCode;

    private String base;

    private String detail;

    private String alias;

    private Boolean isDefault;
}
