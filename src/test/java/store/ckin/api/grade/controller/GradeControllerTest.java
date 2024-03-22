package store.ckin.api.grade.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.grade.domain.request.GradeCreateRequestDto;
import store.ckin.api.grade.domain.request.GradeUpdateRequestDto;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeAlreadyExistsException;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.service.GradeService;

/**
 * GradeController Test Code 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 22.
 */
@WebMvcTest(GradeController.class)
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
class GradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("등급 생성")
    void testCreateGrade() throws Exception {
        GradeCreateRequestDto dto = new GradeCreateRequestDto();

        ReflectionTestUtils.setField(dto, "id", 1L);
        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 5);
        ReflectionTestUtils.setField(dto, "condition", 100000);

        doNothing().when(gradeService).createGrade(dto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/api/admin/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andDo(document("grade/createGrade/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("등급 ID"),
                                fieldWithPath("name").description("등급 이름"),
                                fieldWithPath("pointRatio").description("적립률"),
                                fieldWithPath("condition").description("등급 조건")
                        )
                ));

        verify(gradeService).createGrade(any(GradeCreateRequestDto.class));
    }

    @Test
    @DisplayName("이미 존재하는 등급 ID로 인한 등급 생성 실패")
    void testCreateGradeFailed() throws Exception {
        GradeCreateRequestDto dto = new GradeCreateRequestDto();

        ReflectionTestUtils.setField(dto, "id", 1L);
        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 5);
        ReflectionTestUtils.setField(dto, "condition", 100000);

        doThrow(GradeAlreadyExistsException.class)
                .when(gradeService)
                .createGrade(any(GradeCreateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/admin/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andDo(document("grade/createGrade/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(gradeService).createGrade(any(GradeCreateRequestDto.class));
    }

    @Test
    @DisplayName("모든 등급 조회")
    void testGetGradeList() throws Exception {
        Grade normalGrade = Grade.builder()
                .id(1L)
                .name("일반")
                .pointRatio(5)
                .condition(0)
                .build();

        Grade royalGrade = Grade.builder()
                .id(2L)
                .name("로얄")
                .pointRatio(10)
                .condition(100000)
                .build();

        when(gradeService.getGradeList())
                .thenReturn(List.of(normalGrade, royalGrade));

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/admin/grades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.[0].id").value(normalGrade.getId()),
                        jsonPath("$.[0].name").value(normalGrade.getName()),
                        jsonPath("$.[0].pointRatio").value(normalGrade.getPointRatio()),
                        jsonPath("$.[0].condition").value(normalGrade.getCondition()))
                .andDo(document("grade/getGradeList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("등급 ID"),
                                fieldWithPath("[].name").description("등급 이름"),
                                fieldWithPath("[].pointRatio").description("적립률"),
                                fieldWithPath("[].condition").description("조건")
                        )));

        verify(gradeService).getGradeList();
    }

    @Test
    @DisplayName("등급 변경")
    void testUpdateGrade() throws Exception {
        GradeUpdateRequestDto dto = new GradeUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 20);
        ReflectionTestUtils.setField(dto, "condition", 10000);

        doNothing().when(gradeService).updateGrade(anyLong(), any(GradeUpdateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/api/admin/grades/{gradeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("grade/updateGrade/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("gradeId").description("등급 ID")),
                        requestFields(
                                fieldWithPath("name").description("등급 이름"),
                                fieldWithPath("pointRatio").description("적립률"),
                                fieldWithPath("condition").description("조건")
                        )
                ));

        verify(gradeService).updateGrade(anyLong(), any(GradeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 등급 ID로 인한 등급 변경 실패")
    void testUpdateGradeFailed() throws Exception {
        GradeUpdateRequestDto dto = new GradeUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 20);
        ReflectionTestUtils.setField(dto, "condition", 10000);

        doThrow(GradeNotFoundException.class)
                .when(gradeService)
                .updateGrade(anyLong(), any(GradeUpdateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/admin/grades/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(document("grade/updateGrade/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(gradeService).updateGrade(anyLong(), any(GradeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("등급 삭제")
    void testDeleteGrade() throws Exception {
        doNothing().when(gradeService).deleteGrade(1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete("/api/admin/grades/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("grade/deleteGrade/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("gradeId").description("등급 ID"))
                ));

        verify(gradeService).deleteGrade(1L);
    }

    @Test
    @DisplayName("존재하지 않는 등급 ID로 인한 등급 삭제 실패")
    void testDeleteGradeFailed() throws Exception {
        doThrow(GradeNotFoundException.class)
                .when(gradeService)
                .deleteGrade(1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete("/api/admin/grades/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("grade/deleteGrade/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("gradeId").description("등급 ID"))
                ));

        verify(gradeService).deleteGrade(1L);
    }
}
