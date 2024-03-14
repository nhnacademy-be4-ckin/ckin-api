package store.ckin.api.tag.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 16
 */
@Getter
public class TagDeleteRequestDto {
    @NotNull
    private Long tagId;
}
