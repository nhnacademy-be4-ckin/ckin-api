package store.ckin.api.tag.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.exception.TagNameAlreadyExistException;
import store.ckin.api.tag.exception.TagNotFoundException;
import store.ckin.api.tag.service.impl.TagServiceImpl;

/**
 * Tag Controller Test.
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TagServiceImpl tagService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("태그 목록 가져오기 - 성공")
    void getAllTagListTest() throws Exception {
        // given
        List<TagResponseDto> allElements = List.of(
                new TagResponseDto(1L, "태그1"),
                new TagResponseDto(2L, "태그2")
        );
        PageInfo pageInfo = PageInfo.builder()
                .page(0)
                .size(5)
                .totalPages(1)
                .totalElements(2)
                .build();

        PagedResponse<List<TagResponseDto>> expected = new PagedResponse<>(allElements, pageInfo);
        given(tagService.readTagList(PageRequest.of(0, 5))).willReturn(expected);

        // when then
        mockMvc.perform(get("/api/tags")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected))
                )
                .andDo(document("tag/getTagList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("지정할 페이지"),
                                parameterWithName("size").description("한 페이지 당 표시할 개수")
                        ),
                        responseFields(
                                fieldWithPath("data.[].tagId").description("저장된 태그 아이디"),
                                fieldWithPath("data.[].tagName").description("저장된 태그 이름"),
                                fieldWithPath("pageInfo.page").description("현재 페이지 정보"),
                                fieldWithPath("pageInfo.size").description("지정한 페이지 크기"),
                                fieldWithPath("pageInfo.totalElements").description("총 데이터 수"),
                                fieldWithPath("pageInfo.totalPages").description("총 페이지 수")
                        )

                ));

        verify(tagService, times(1)).readTagList(any());
    }

    @Test
    @DisplayName("태그 저장 - 실패(Validation Error)")
    void saveTagTest_Failed_Validation() throws Exception {
        // given
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(tagCreateRequestDto, "tagName", "12345678910");

        // when
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("tag/saveTag/validation-failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("태그 저장 - 실패(이미 존재하는 태그 이름)")
    void saveTagTest_Failed_AlreadyExist() throws Exception {
        // given
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto();
        ReflectionTestUtils.setField(tagCreateRequestDto, "tagName", "태그1");

        TagNameAlreadyExistException expectedException = new TagNameAlreadyExistException();

        willThrow(expectedException).given(tagService).createTag(any());

        // when
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpectAll(
                        jsonPath("code", equalTo("CONFLICT")),
                        jsonPath("message", equalTo(expectedException.getMessage())),
                        status().isConflict()
                ).andDo(document("tag/saveTag/already-exist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        verify(tagService, times(1)).createTag(any());
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
                .andExpect(status().isCreated())
                .andDo(document("tag/saveTag/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("tagName").description("저장할 태그 이름")
                        )));
        verify(tagService, times(1)).createTag(any());
    }

    @Test
    @DisplayName("태그 수정 - 실패(Validation Error)")
    void updateTagTest_Failed() throws Exception {
        // given
        TagUpdateRequestDto tagUpdateRequestDto = new TagUpdateRequestDto();
        ReflectionTestUtils.setField(tagUpdateRequestDto, "tagId", 1L);
        ReflectionTestUtils.setField(tagUpdateRequestDto, "tagName", "12345678910");

        // when
        mockMvc.perform(put("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("tag/updateTag/validation-failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
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
                .andExpect(status().isOk())
                .andDo(document("tag/updateTag/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("tagId").description("태그 수정을 위한 태그 아이디"),
                                fieldWithPath("tagName").description("태그 수정을 위한 태그 이름")
                        )));
        verify(tagService, times(1)).updateTag(any());
    }

    @Test
    @DisplayName("태그 삭제 - 실패(Validation Error)")
    void deleteTagTest_Failed_Validation() throws Exception {
        // given
        TagDeleteRequestDto tagDeleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(tagDeleteRequestDto, "tagId", null);

        // when
        mockMvc.perform(delete("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDeleteRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("tag/deleteTag/validation-failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("태그 삭제 - 실패(존재하지 않는 태그)")
    void deleteTagTest_Failed_TagNotFoundException() throws Exception {
        // given
        TagDeleteRequestDto tagDeleteRequestDto = new TagDeleteRequestDto();
        ReflectionTestUtils.setField(tagDeleteRequestDto, "tagId", 1L);
        TagNotFoundException expectedException = new TagNotFoundException();
        willThrow(expectedException).given(tagService).deleteTag(any());

        // when
        mockMvc.perform(delete("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDeleteRequestDto)))
                .andExpectAll(
                        jsonPath("code", equalTo("NOT_FOUND")),
                        jsonPath("message", equalTo(expectedException.getMessage())),
                        status().isNotFound())
                .andDo(document("tag/deleteTag/not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        verify(tagService, times(1)).deleteTag(any());
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
                .andExpect(status().isOk())
                .andDo(document("tag/deleteTag/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("tagId").description("삭제를 위한 태그 아이디")
                        )));
        verify(tagService, times(1)).deleteTag(any());
    }
}