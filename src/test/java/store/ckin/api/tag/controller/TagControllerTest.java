package store.ckin.api.tag.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import store.ckin.api.error.ErrorResponse;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.exception.TagNameAlreadyExistException;
import store.ckin.api.tag.exception.TagNotFoundException;
import store.ckin.api.tag.service.impl.TagServiceImpl;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private TagController tagController;
    @Mock
    private TagServiceImpl tagService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    @DisplayName("태그 목록 가져오기 - 성공")
    void getAllTagListTest() throws Exception{
        // given
        List<TagResponseDto> expected = List.of(
                new TagResponseDto(1L, "태그1"),
                new TagResponseDto(2L, "태그2")
        );

        given(tagService.readTagList()).willReturn(expected);

        // when
        mockMvc.perform(get("/api/tags"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                );
    }

    @Test
    @DisplayName("태그 저장 - 실패(Validation Error)")
    void saveTagTest_Failed_Validation() throws Exception{
        // given
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(tagCreateRequestDto, "tagName", "12345678910");

        // when
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("태그 저장 - 실패(이미 존재하는 태그 이름)")
    void saveTagTest_Failed_AlreadyExist() throws Exception {
        // given
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(tagCreateRequestDto, "tagName", "태그1");
        TagNameAlreadyExistException expectedException = new TagNameAlreadyExistException(tagCreateRequestDto.getTagName());
        willThrow(expectedException).given(tagService).createTag(any());

        // when
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpectAll(
                        jsonPath("code", equalTo("TagName Already Exist")),
                        jsonPath("message", equalTo(expectedException.getMessage())),
                        status().isBadRequest()
                );
    }

    @Test
    @DisplayName("태그 저장 - 성공")
    void saveTagTest_Success() throws Exception {
        // given
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(tagCreateRequestDto, "tagName", "태그1");

        // when
        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("태그 수정 - 실패(Validation Error)")
    void updateTagTest_Failed() throws Exception{
        // given
        TagUpdateRequestDto tagUpdateRequestDto = new TagUpdateRequestDto();
        ReflectionTestUtils.setField(tagUpdateRequestDto, "tagId", 1L);
        ReflectionTestUtils.setField(tagUpdateRequestDto, "tagName", "12345678910");

        // when
        mockMvc.perform(put("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagUpdateRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("태그 수정 - 성공")
    void updateTagTest_Success() throws Exception {
        // given
        TagUpdateRequestDto tagUpdateRequestDto = new TagUpdateRequestDto();
        ReflectionTestUtils.setField(tagUpdateRequestDto, "tagId", 1L);
        ReflectionTestUtils.setField(tagUpdateRequestDto, "tagName", "태그1");

        // when
        mockMvc.perform(put("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagUpdateRequestDto)))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("태그 삭제 - 실패(Validation Error)")
    void deleteTagTest_Failed_Validation() throws Exception{
        // given
        TagDeleteRequestDto tagDeleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(tagDeleteRequestDto, "tagId", null);

        // when
        mockMvc.perform(delete("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDeleteRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("태그 삭제 - 실패(존재하지 않는 태그)")
    void deleteTagTest_Failed_TagNotFoundException() throws Exception{
        // given
        TagDeleteRequestDto tagDeleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(tagDeleteRequestDto, "tagId", 1L);
        TagNotFoundException expectedException = new TagNotFoundException(tagDeleteRequestDto.getTagId());
        willThrow(expectedException).given(tagService).deleteTag(any());

        // when
        mockMvc.perform(delete("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDeleteRequestDto)))
                .andExpectAll(
                        jsonPath("code", equalTo("Tag Not Found")),
                        jsonPath("message", equalTo(expectedException.getMessage())),
                        status().isNotFound());
    }

    @Test
    @DisplayName("태그 삭제 - 성공")
    void deleteTagTest_Success() throws Exception {
        // given
        TagDeleteRequestDto tagDeleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(tagDeleteRequestDto, "tagId", 1L);

        // when
        mockMvc.perform(delete("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDeleteRequestDto)))
                .andExpect(status().isOk());
    }
}