package store.ckin.api.tag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.entity.Tag;
import store.ckin.api.tag.exception.TagNameAlreadyExistException;
import store.ckin.api.tag.exception.TagNotFoundException;
import store.ckin.api.tag.repository.TagRepository;

/**
 * TagServiceImpl 클래스 테스팅 클래스
 *
 * @author 김준현
 * @version 2024. 02. 17
 */

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("태그 리스트 조회 - 성공")
    void readTagListTest() throws Exception{
        // given
        List<TagResponseDto> testCases = new ArrayList<>();
        for(int i=1; i<=3; i++) {
            TagResponseDto tagResponseDto = new TagResponseDto((long) i, "태그" + i);
            testCases.add(tagResponseDto);
        }

        // when
        when(tagRepository.findAllTags())
                .thenReturn(testCases);
        List<TagResponseDto> actual = tagService.readTagList();

        // then
        assertEquals(3, actual.size());
        assertEquals(1L, actual.get(0).getTagId());
        assertEquals(2L, actual.get(1).getTagId());
        assertEquals(3L, actual.get(2).getTagId());
    }

    @Test
    @DisplayName("태그 생성 - 실패(이미 존재하는 태그)")
    void createTagTest_Fail() throws Exception{
        // given
        given(tagRepository.existsByTagName(anyString()))
                .willReturn(true);

        TagCreateRequestDto createRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(createRequestDto, "tagName", "중복");


        // when
        assertThrows(TagNameAlreadyExistException.class, () -> tagService.createTag(createRequestDto));
    }

    @Test
    @DisplayName("태그 생성 - 성공")
    void createTagTest_Success() throws Exception {
        // given
        given(tagRepository.existsByTagName(anyString()))
                .willReturn(false);

        TagCreateRequestDto createRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(createRequestDto, "tagName", "태그1");

        // when
        tagService.createTag(createRequestDto);

        // then
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("태그 수정 - 실패(존재하지 않는 태그)")
    void updateTagTest_Fail() throws Exception{
        // given
        given(tagRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        TagUpdateRequestDto updateRequestDto = new TagUpdateRequestDto();
        ReflectionTestUtils.setField(updateRequestDto, "tagId", 1L);

        // when
        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(updateRequestDto));
    }

    @Test
    @DisplayName("태그 수정 - 성공")
    void updateTagTest_Success() throws Exception {
        // given
        Tag tag = Tag.builder()
                .tagId(1L)
                .tagName("태그1")
                .build();
        given(tagRepository.findById(anyLong()))
                .willReturn(Optional.of(tag));

        TagUpdateRequestDto updateRequestDto = new TagUpdateRequestDto();
        ReflectionTestUtils.setField(updateRequestDto, "tagId", 1L);
        ReflectionTestUtils.setField(updateRequestDto, "tagName", "수정-태그1");

        // when
        tagService.updateTag(updateRequestDto);

        // then
        assertEquals(updateRequestDto.getTagId(), tag.getTagId());
        assertEquals(updateRequestDto.getTagName(), tag.getTagName());
    }

    @Test
    @DisplayName("태그 삭제 - 실패(존재하지 않는 태그)")
    void deleteTag_Fail() throws Exception{
        // given
        given(tagRepository.existsById(anyLong()))
                .willReturn(false);

        TagDeleteRequestDto deleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(deleteRequestDto, "tagId", 1L);

        // when
        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(deleteRequestDto));
    }

    @Test
    @DisplayName("태그 삭제 - 성공")
    void deleteTag_Success() throws Exception {
        // given
        given(tagRepository.existsById(anyLong()))
                .willReturn(true);

        TagDeleteRequestDto deleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(deleteRequestDto, "tagId", 1L);

        // when
        tagService.deleteTag(deleteRequestDto);

        // then
        verify(tagRepository, times(1)).deleteById(1L);
    }
}