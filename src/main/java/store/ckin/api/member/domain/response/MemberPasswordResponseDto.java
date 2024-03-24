package store.ckin.api.member.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 요청에 대한 응답 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 24.
 */
@Getter
@NoArgsConstructor
public class MemberPasswordResponseDto {
    private String password;
}
