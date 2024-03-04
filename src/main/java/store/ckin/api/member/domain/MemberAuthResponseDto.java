package store.ckin.api.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.member.entity.Member;

/**
 * 로그인 요청에 대한 응답 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@Getter
@NoArgsConstructor
public class MemberAuthResponseDto {
    Long id;

    String email;

    String password;

    String role;

    /**
     * Member.Role 값을 String 으로 변환하는 생성자 메서드 입니다.
     *
     * @param id       the id
     * @param email    the email
     * @param password the password
     * @param role     the role
     */
    public MemberAuthResponseDto(Long id, String email, String password, Member.Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role.name();
    }
}
