package store.ckin.api.tag.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 16
 */
@AllArgsConstructor
@Getter
public class TagResponseDto {
    private Long tagId;
    private String tagName;
}
