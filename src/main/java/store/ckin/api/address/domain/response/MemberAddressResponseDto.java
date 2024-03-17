package store.ckin.api.address.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특정 멤버의 주소 정보를 응답하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
@Getter
@NoArgsConstructor
public class MemberAddressResponseDto {
    private String base;

    private String detail;

    private String alias;

    private Boolean isDefault;
}
