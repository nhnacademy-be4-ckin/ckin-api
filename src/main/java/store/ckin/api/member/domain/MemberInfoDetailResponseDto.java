package store.ckin.api.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SecurityContextHolder 에 담을 멤버 정보 요청에 대한 응답 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 04.
 */
@Getter
@NoArgsConstructor
public class MemberInfoDetailResponseDto {
    String email;

    String role;
}
