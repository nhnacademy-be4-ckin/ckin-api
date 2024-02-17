package store.ckin.api.tag.service;

import java.util.List;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;

/**
 * description:
 *
 * @author : 김준현
 * @version : 2024. 02. 14
 */
public interface TagService {
    /**
     *
     * @return
     */
    List<TagResponseDto> readTagList();

    /**
     *
     */
    void createTag(TagCreateRequestDto tagCreateRequestDto);

    /**
     *
     */
    void updateTag(TagUpdateRequestDto tagUpdateRequestDto);
    /**
     *
     */
    void deleteTag(TagDeleteRequestDto tagDeleteRequestDto);
}
