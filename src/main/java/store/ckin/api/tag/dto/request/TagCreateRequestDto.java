package store.ckin.api.tag.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;

/**
 * 태그 생성을 위한 요청 DTO
 *
 * @author 김준현
 * @version 2024. 02. 16
 */
@Getter
public class TagCreateRequestDto {
    @NotNull(message = "태그 이름을 입력해 주세요")
    @Size(min = 2, max = 10, message = "태그 이름은 2자 이상 10자 이하로 입력해주세요")
    private String tagName;
}
