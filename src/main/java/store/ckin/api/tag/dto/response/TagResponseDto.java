package store.ckin.api.tag.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 조건에 맞는 태그 정보를 응답하기 위한 Dto 객체
 *
 * @author 김준현
 * @version 2024. 02. 16
 */
@Getter
@AllArgsConstructor
public class TagResponseDto {
    private Long tagId;
    private String tagName;
}
