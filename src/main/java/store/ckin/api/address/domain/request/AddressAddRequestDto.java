package store.ckin.api.address.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주소 추가를 요청하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
@Getter
@NoArgsConstructor
public class AddressAddRequestDto {
    private String base;

    private String detail;

    private String alias;
}
