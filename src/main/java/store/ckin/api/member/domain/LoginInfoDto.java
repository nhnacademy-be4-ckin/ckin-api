package store.ckin.api.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Login 할 때 필요한 정보를 가져오는 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Getter
@NoArgsConstructor
public class LoginInfoDto {
    private String email;

    private String password;
}
