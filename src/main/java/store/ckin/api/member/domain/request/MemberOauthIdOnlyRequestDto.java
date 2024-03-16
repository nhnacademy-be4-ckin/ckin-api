package store.ckin.api.member.domain.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OAuth ID 만으로 요청하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
@Getter
@NoArgsConstructor
public class MemberOauthIdOnlyRequestDto {
    @NotBlank
    private String oauthId;
}
