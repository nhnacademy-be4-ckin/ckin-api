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
public class MemberInfoResponseDto {
    String email;

    String password;

    Member.Role role;
}
