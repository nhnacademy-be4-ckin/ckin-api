package store.ckin.api.pointpolicy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(PointPolicyController.class)
class PointPolicyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PointPolicyService pointPolicyService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("포인트 정책 개별 조회")
    void testGetPointPolicy() throws Exception {

        given(pointPolicyService.getPointPolicy(anyLong()))
                .willReturn(new PointPolicyResponseDto(1L, "정책1", 3000));

        mockMvc.perform(get("/api/point-policies/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.pointPolicyId", equalTo(1)),
                        jsonPath("$.pointPolicyName", equalTo("정책1")),
                        jsonPath("$.pointPolicyReserve", equalTo(3000)))
                .andDo(print());

        verify(pointPolicyService, times(1)).getPointPolicy(anyLong());
    }

    @Test
    @DisplayName("포인트 정책 리스트 조회")
    void testGetPointPolicies() throws Exception {

        given(pointPolicyService.getPointPolicies())
                .willReturn(List.of(new PointPolicyResponseDto(1L, "정책1", 300),
                        new PointPolicyResponseDto(2L, "정책2", 666)));

        mockMvc.perform(get("/api/point-policies"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].pointPolicyId", equalTo(1)),
                        jsonPath("$[0].pointPolicyName", equalTo("정책1")),
                        jsonPath("$[1].pointPolicyId", equalTo(2)),
                        jsonPath("$[1].pointPolicyReserve", equalTo(666)))
                .andDo(print());

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
                .andDo(print());

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
                .andDo(print());
    }

    @Test
    @DisplayName("포인트 정책 수정 - 성공")
    void testUpdatePointPolicy_Success() throws Exception {

        PointPolicyUpdateRequestDto pointPolicy = new PointPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyId", 1L);
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyName", "포인트 정책");
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyReserve", 300);

        String json = new ObjectMapper().writeValueAsString(pointPolicy);

        mockMvc.perform(put("/api/point-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        verify(pointPolicyService, times(1)).updatePointPolicy(any());
    }

    @Test
    @DisplayName("포인트 정책 수정 - 실패(Validation)")
    void testUpdatePointPolicy_Fail() throws Exception {

        PointPolicyUpdateRequestDto pointPolicy = new PointPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyId", null);
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyName", "");
        ReflectionTestUtils.setField(pointPolicy, "pointPolicyReserve", -3123213);

        String json = objectMapper.writeValueAsString(pointPolicy);

        mockMvc.perform(put("/api/point-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("포인트 정책 삭제")
    void testDeletePointPolicy() throws Exception {

        mockMvc.perform(delete("/api/point-policies/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print());

        verify(pointPolicyService, times(1)).deletePointPolicy(anyLong());
    }
}