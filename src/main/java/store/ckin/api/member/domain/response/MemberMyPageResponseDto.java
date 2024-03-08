package store.ckin.api.member.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 마이페이지 정보 요청에 대한 응답 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 08.
 */
@Getter
@AllArgsConstructor
public class MemberMyPageResponseDto {
    private String name;

    private String gradeName;

    private Integer accumulateAmount;

    private Integer point;
}
