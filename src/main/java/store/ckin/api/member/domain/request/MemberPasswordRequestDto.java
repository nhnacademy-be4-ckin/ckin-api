package store.ckin.api.member.domain.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 변경을 요청 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 24.
 */
@Getter
@NoArgsConstructor
public class MemberPasswordRequestDto {
    @NotBlank
    private String password;
}
