package store.ckin.api.member.domain.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Member Email 만으로 요청하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 20.
 */
@Getter
@NoArgsConstructor
public class MemberEmailOnlyRequestDto {
    @Email
    @NotBlank
    private String email;
}
