package store.ckin.api.member.domain;

import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Member 생성을 할 때 필요한 정보를 가져오는 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Getter
@NoArgsConstructor
public class MemberCreateRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Length(min = 2, max = 10)
    private String name;

    @NotBlank
    @Length(min = 10, max = 11)
    private String contact;

    @NotNull
    private LocalDateTime birth;
}
