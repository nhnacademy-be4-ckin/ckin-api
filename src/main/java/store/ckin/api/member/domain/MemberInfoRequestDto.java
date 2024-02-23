package store.ckin.api.member.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인할 때 필요한 정보를 가져오는 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 19.
 */
@Getter
@NoArgsConstructor
public class MemberInfoRequestDto {
    @Email
    @NotBlank
    private String email;
}
