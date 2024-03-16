package store.ckin.api.member.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.member.entity.Member;

/**
 * OAuth Login 요청에 응답하는 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
@Getter
@NoArgsConstructor
public class MemberOauthLoginResponseDto {
    private Long id;

    private String role;

    public MemberOauthLoginResponseDto(Long id, Member.Role role) {
        this.id = id;
        this.role = role.name();
    }
}
