package store.ckin.api.member.domain.request;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 회원 정보 수정 요청 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 24.
 */
@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @NotBlank
    @Size(min = 10, max = 11)
    private String contact;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}
