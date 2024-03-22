package store.ckin.api.pointpolicy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.pointpolicy.dto.request.PointPolicyCreateRequestDto;
import store.ckin.api.pointpolicy.dto.request.PointPolicyUpdateRequestDto;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.service.PointPolicyService;

/**
 * 포인트 정책 컨트롤러 테스트 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
@WebMvcTest(PointPolicyController.class)
class PointPolicyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PointPolicyService pointPolicyService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("포인트 정책 개별 조회")
    void testGetPointPolicy() throws Exception {

        PointPolicyResponseDto pointPolicy = new PointPolicyResponseDto(1L, "회원가입", 3000);

        given(pointPolicyService.getPointPolicy(anyLong()))
                .willReturn(pointPolicy);

        mockMvc.perform(get("/api/point-policies/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.pointPolicyId").value(pointPolicy.getPointPolicyId()),
                        jsonPath("$.pointPolicyName").value(pointPolicy.getPointPolicyName()),
                        jsonPath("$.pointPolicyReserve").value(pointPolicy.getPointPolicyReserve()))
                .andDo(document("point-policy/getPointPolicy/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("pointPolicyId").description("포인트 정책 ID"),
                                fieldWithPath("pointPolicyName").description("포인트 정책 명"),
                                fieldWithPath("pointPolicyReserve").description("포인트 정책 적립 금액")
                        )
                ));

        verify(pointPolicyService, times(1)).getPointPolicy(anyLong());
    }

    @Test
    @DisplayName("포인트 정책 리스트 조회")
    void testGetPointPolicies() throws Exception {

        PointPolicyResponseDto firstPointPolicy = new PointPolicyResponseDto(1L, "회원가입", 5000);
        PointPolicyResponseDto secondPointPolicy = new PointPolicyResponseDto(2L, "리뷰작성", 300);

        given(pointPolicyService.getPointPolicies())
                .willReturn(List.of(firstPointPolicy, secondPointPolicy));

        mockMvc.perform(get("/api/point-policies"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].pointPolicyId").value(firstPointPolicy.getPointPolicyId()),
                        jsonPath("$[0].pointPolicyName").value(firstPointPolicy.getPointPolicyName()),
                        jsonPath("$[0].pointPolicyReserve").value(firstPointPolicy.getPointPolicyReserve()),
                        jsonPath("$[1].pointPolicyId").value(secondPointPolicy.getPointPolicyId()),
                        jsonPath("$[1].pointPolicyName").value(secondPointPolicy.getPointPolicyName()),
                        jsonPath("$[1].pointPolicyReserve").value(secondPointPolicy.getPointPolicyReserve()))
                .andDo(document("point-policy/getPointPolicyList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].pointPolicyId").description("포인트 정책 ID"),
                                fieldWithPath("[].pointPolicyName").description("포인트 정책 명"),
                                fieldWithPath("[].pointPolicyReserve").description("포인트 정책 적립 금액")
                        )
                ));

        verify(pointPolicyService, times(1)).getPointPolicies();

    }

    @Test
    @DisplayName("포인트 정책 생성 - 성공")
    void testCreatePointPolicy_Success() throws Exception {

        PointPolicyCreateRequestDto pointPolicy = new PointPolicyCreateRequestDto();
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyId", 1L);
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyName", "포인트 정책");
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyReserve", 300);

        String json = objectMapper.writeValueAsString(pointPolicy);

        mockMvc.perform(post("/api/point-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(document("point-policy/createPointPolicy/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("pointPolicyId").description("포인트 정책 ID"),
                                fieldWithPath("pointPolicyName").description("포인트 정책 명"),
                                fieldWithPath("pointPolicyReserve").description("포인트 정책 적립 금액")
                        )));

        verify(pointPolicyService, times(1)).createPointPolicy(any());
    }

    @Test
    @DisplayName("포인트 정책 생성 - 실패(Validation)")
    void testCreatePointPolicy_Fail() throws Exception {

        PointPolicyCreateRequestDto pointPolicy = new PointPolicyCreateRequestDto();
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyId", null);
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyName", "");
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyReserve", -3123213);

        String json = objectMapper.writeValueAsString(pointPolicy);

        mockMvc.perform(post("/api/point-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(document("point-policy/createPointPolicy/validation-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("pointPolicyId").description("포인트 정책 ID"),
                                fieldWithPath("pointPolicyName").description("포인트 정책 명"),
                                fieldWithPath("pointPolicyReserve").description("포인트 정책 적립 금액")
                        )));

        verify(pointPolicyService, times(0)).createPointPolicy(any());
    }

    @Test
    @DisplayName("포인트 정책 수정 - 성공")
    void testUpdatePointPolicy_Success() throws Exception {

        PointPolicyUpdateRequestDto pointPolicy = new PointPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyName", "포인트 정책");
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyReserve", 300);

        String json = objectMapper.writeValueAsString(pointPolicy);

        mockMvc.perform(put("/api/point-policies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("point-policy/updatePointPolicy/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("pointPolicyName").description("포인트 정책 명"),
                                fieldWithPath("pointPolicyReserve").description("포인트 정책 적립 금액")
                        )));

        verify(pointPolicyService, times(1)).updatePointPolicy(anyLong(), any());
    }

    @Test
    @DisplayName("포인트 정책 수정 - 실패(Validation)")
    void testUpdatePointPolicy_Fail() throws Exception {

        PointPolicyUpdateRequestDto pointPolicy = new PointPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyName", "");
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyReserve", -3123213);

        String json = objectMapper.writeValueAsString(pointPolicy);

        mockMvc.perform(put("/api/point-policies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(document("point-policy/updatePointPolicy/validation-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("pointPolicyName").description("포인트 정책 명"),
                                fieldWithPath("pointPolicyReserve").description("포인트 정책 적립 금액")
                        )
                ));

        verify(pointPolicyService, times(0)).updatePointPolicy(anyLong(), any());
    }

    @Test
    @DisplayName("포인트 정책 삭제")
    void testDeletePointPolicy() throws Exception {

        mockMvc.perform(delete("/api/point-policies/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(document("point-policy/deletePointPolicy/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                );

        verify(pointPolicyService, times(1)).deletePointPolicy(anyLong());
    }
}