package store.ckin.api.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원의 포인트 응답 DTO 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 05.
 */

@Getter
@AllArgsConstructor
public class MemberPointResponseDto {

    private Integer point;
}
