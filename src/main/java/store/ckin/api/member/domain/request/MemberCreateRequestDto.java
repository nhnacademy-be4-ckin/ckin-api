package store.ckin.api.member.domain.request;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

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
    private String password;

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @NotBlank
    @Size(min = 10, max = 11)
    private String contact;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Nullable
    private String oauthId;
}
