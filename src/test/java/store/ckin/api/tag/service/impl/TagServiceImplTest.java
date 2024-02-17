package store.ckin.api.tag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
        Field[] fields = TagResponseDto.class.getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
        }
        List<TagResponseDto> testCases = new ArrayList<>();
        for(int i=1; i<=3; i++) {
            TagResponseDto tagResponseDto = new TagResponseDto();
            fields[0].set(tagResponseDto, (long) i);
            fields[1].set(tagResponseDto, "태그" + i);

            testCases.add(tagResponseDto);
        }

        when(tagRepository.findAllTags())
                .thenReturn(testCases);

        List<TagResponseDto> actual = tagService.readTagList();
        assertEquals(3, actual.size());

        assertEquals(1L, actual.get(0).getTagId());
        assertEquals(2L, actual.get(1).getTagId());
        assertEquals(3L, actual.get(2).getTagId());
    }

    @Test
    @DisplayName("태그 생성 - 실패(이미 존재하는 태그)")
    void createTagTest_Fail() throws Exception{
        when(tagRepository.existsByTagName(anyString()))
                .thenReturn(true);

        Field tagCreateRequestName = TagCreateRequestDto.class.getDeclaredField("tagName");
        TagCreateRequestDto createRequestDto = new TagCreateRequestDto();
        tagCreateRequestName.setAccessible(true);
        tagCreateRequestName.set(createRequestDto, "중복 태그");

        assertThrows(TagNameAlreadyExistException.class, () -> tagService.createTag(createRequestDto));
    }

    @Test
    @DisplayName("태그 생성 - 성공")
    void createTagTest_Success() throws Exception {
        when(tagRepository.existsByTagName(anyString()))
                .thenReturn(false);

        Field[] fields = TagCreateRequestDto.class.getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
        }
        TagCreateRequestDto createRequestDto = new TagCreateRequestDto();
        fields[0].set(createRequestDto, "태그1");

        tagService.createTag(createRequestDto);
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("태그 수정 - 실패(존재하지 않는 태그)")
    void updateTagTest_Fail() throws Exception{
        when(tagRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Field[] fields = TagUpdateRequestDto.class.getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
        }
        TagUpdateRequestDto updateRequestDto = new TagUpdateRequestDto();
        fields[0].set(updateRequestDto, 1L);

        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(updateRequestDto));
    }

    @Test
    @DisplayName("태그 수정 - 성공")
    void updateTagTest_Success() throws Exception {
        Tag tag = Tag.builder()
                        .tagId(1L)
                                .tagName("태그1")
                                        .build();
        when(tagRepository.findById(anyLong()))
                .thenReturn(Optional.of(tag));

        Field[] fields = TagUpdateRequestDto.class.getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
        }
        TagUpdateRequestDto updateRequestDto = new TagUpdateRequestDto();
        fields[0].set(updateRequestDto, 1L);
        fields[1].set(updateRequestDto, "태그1수정");

        tagService.updateTag(updateRequestDto);
        assertEquals(updateRequestDto.getTagId(), tag.getTagId());
        assertEquals(updateRequestDto.getTagName(), tag.getTagName());
    }

    @Test
    @DisplayName("태그 삭제 - 실패(존재하지 않는 태그)")
    void deleteTag_Fail() throws Exception{
        when(tagRepository.existsById(anyLong()))
                .thenReturn(false);

        TagDeleteRequestDto deleteRequestDto = new TagDeleteRequestDto();
        Field tagDeleteRequestId = TagDeleteRequestDto.class.getDeclaredField("tagId");
        tagDeleteRequestId.setAccessible(true);
        tagDeleteRequestId.set(deleteRequestDto, 1L);

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(deleteRequestDto));
    }

    @Test
    @DisplayName("태그 삭제 - 성공")
    void deleteTag_Success() throws Exception {
        when(tagRepository.existsById(anyLong()))
                .thenReturn(true);

        TagDeleteRequestDto deleteRequestDto = new TagDeleteRequestDto();
        Field tagDeleteRequestId = TagDeleteRequestDto.class.getDeclaredField("tagId");
        tagDeleteRequestId.setAccessible(true);
        tagDeleteRequestId.set(deleteRequestDto, 1L);

        tagService.deleteTag(deleteRequestDto);

        verify(tagRepository, times(1)).deleteById(1L);
    }
}