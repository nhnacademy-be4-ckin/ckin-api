package store.ckin.api.tag.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.entity.Tag;
import store.ckin.api.tag.exception.TagNameAlreadyExistException;
import store.ckin.api.tag.exception.TagNotFoundException;
import store.ckin.api.tag.repository.TagRepository;
import store.ckin.api.tag.service.TagService;

/**
 * 태그 서비스 구현 클래스
 *
 * @author : 김준현
 * @version : 2024. 02. 14
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public PagedResponse<List<TagResponseDto>> readTagList(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAllByOrderByTagId(pageable);
        PageInfo pageInfo = PageInfo.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements((int) tagPage.getTotalElements())
                .totalPages(tagPage.getTotalPages())
                .build();

        List<TagResponseDto> currentPageTagsResponse = tagPage.getContent().stream().map(TagResponseDto::toDto).collect(
                Collectors.toList());

        return new PagedResponse<>(currentPageTagsResponse, pageInfo);
    }

    @Transactional
    public void createTag(TagCreateRequestDto tagCreateRequestDto) {
        if(tagRepository.existsByTagName(tagCreateRequestDto.getTagName())) {
            throw new TagNameAlreadyExistException(tagCreateRequestDto.getTagName());
        } else {
            Tag tag = Tag.builder()
                    .tagName(tagCreateRequestDto.getTagName())
                    .build();
            tagRepository.save(tag);
        }
    }

    @Transactional
    public void updateTag(TagUpdateRequestDto tagUpdateRequestDto) {
        Optional<Tag> tagWrapped = tagRepository.findById(tagUpdateRequestDto.getTagId());
        if(tagWrapped.isPresent()) {
            Tag tag = tagWrapped.get();
            tag.updateTagName(tagUpdateRequestDto.getTagName());
        } else {
            throw new TagNotFoundException(tagUpdateRequestDto.getTagId());
        }
    }

    @Transactional
    public void deleteTag(TagDeleteRequestDto tagDeleteRequestDto) {
        if(tagRepository.existsById(tagDeleteRequestDto.getTagId())) {
            tagRepository.deleteById(tagDeleteRequestDto.getTagId());
        } else {
            throw new TagNotFoundException(tagDeleteRequestDto.getTagId());
        }
    }
}
