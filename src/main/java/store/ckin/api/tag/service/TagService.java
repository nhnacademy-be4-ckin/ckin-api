package store.ckin.api.tag.service;

import org.springframework.data.domain.Pageable;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;

import java.util.List;

/**
 * 태그 서비스 인터페이스
 *
 * @author : 김준현
 * @version : 2024. 02. 14
 */
public interface TagService {
    /**
     * 저장된 모든 태그 목록을 읽어오는 메서드
     *
     * @return 저장된 모든 태그 목록
     */
    PagedResponse<List<TagResponseDto>> readTagList(Pageable pageable);

    /**
     * 단일 태그를 저장하는 메서드
     */
    void createTag(TagCreateRequestDto tagCreateRequestDto);

    /**
     * 단일 태그의 이름을 변경하는 메서드
     */
    void updateTag(TagUpdateRequestDto tagUpdateRequestDto);

    /**
     * 단일 태그를 삭제하는 메서드
     */
    void deleteTag(TagDeleteRequestDto tagDeleteRequestDto);
}
