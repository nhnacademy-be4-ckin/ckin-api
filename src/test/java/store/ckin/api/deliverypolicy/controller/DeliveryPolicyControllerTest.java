package store.ckin.api.deliverypolicy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyCreateRequestDto;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyUpdateRequestDto;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.service.DeliveryPolicyService;

/**
 * 배송비 정책 컨트롤러 테스트 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@WebMvcTest(DeliveryPolicyController.class)
class DeliveryPolicyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DeliveryPolicyService deliveryPolicyService;

    @Test
    @DisplayName("배송비 정책 개별 조회 - 성공")
    void testGetDeliveryPolicy_Success() throws Exception {

        DeliveryPolicyResponseDto deliveryPolicy
                = new DeliveryPolicyResponseDto(1L, 5000, 10000, true);

        given(deliveryPolicyService.getDeliveryPolicy(anyLong()))
                .willReturn(deliveryPolicy);

        mockMvc.perform(get("/api/delivery-policies/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.deliveryPolicyId", equalTo(1)),
                        jsonPath("$.deliveryPolicyFee", equalTo(deliveryPolicy.getDeliveryPolicyFee())),
                        jsonPath("$.deliveryPolicyCondition", equalTo(deliveryPolicy.getDeliveryPolicyCondition())),
                        jsonPath("$.deliveryPolicyState", equalTo(deliveryPolicy.getDeliveryPolicyState())))
                .andDo(print());

        verify(deliveryPolicyService, times(1)).getDeliveryPolicy(anyLong());
    }

    @Test
    @DisplayName("배송비 정책 리스트 조회")
    void testGetDeliveryPolicies() throws Exception {

        List<DeliveryPolicyResponseDto> deliveryPolicies
                = List.of(
                new DeliveryPolicyResponseDto(1L, 5000, 10000, true),
                new DeliveryPolicyResponseDto(2L, 1000, 3000, false));

        given(deliveryPolicyService.getDeliveryPolicies())
                .willReturn(deliveryPolicies);

        mockMvc.perform(get("/api/delivery-policies"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].deliveryPolicyId", equalTo(1)),
                        jsonPath("$[1].deliveryPolicyId", equalTo(2)),
                        jsonPath("$[0].deliveryPolicyFee", equalTo(5000)),
                        jsonPath("$[1].deliveryPolicyCondition", equalTo(3000)),
                        jsonPath("$[0].deliveryPolicyState", equalTo(true)),
                        jsonPath("$[1].deliveryPolicyState", equalTo(false))
                );

        verify(deliveryPolicyService, times(1)).getDeliveryPolicies();
    }

    @Test
    @DisplayName("배송비 정책 생성 - 성공")
    void testCreateDeliveryPolicy_Success() throws Exception {

        DeliveryPolicyCreateRequestDto createDto = new DeliveryPolicyCreateRequestDto();
        ReflectionTestUtils.setField(createDto, "deliveryPolicyFee", 5000);
        ReflectionTestUtils.setField(createDto, "deliveryPolicyCondition", 10000);
        ReflectionTestUtils.setField(createDto, "deliveryPolicyState", true);

        String json = new ObjectMapper().writeValueAsString(createDto);

        mockMvc.perform(post("/api/delivery-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(deliveryPolicyService, times(1)).createDeliveryPolicy(any());
    }

    @Test
    @DisplayName("배송비 정책 생성 - 실패(Validation)")
    void testCreateDeliveryPolicy_Fail() throws Exception {

        DeliveryPolicyCreateRequestDto createDto = new DeliveryPolicyCreateRequestDto();
        ReflectionTestUtils.setField(createDto, "deliveryPolicyFee", -5000);
        ReflectionTestUtils.setField(createDto, "deliveryPolicyCondition", -10);
        ReflectionTestUtils.setField(createDto, "deliveryPolicyState", null);

        String json = new ObjectMapper().writeValueAsString(createDto);

        mockMvc.perform(post("/api/delivery-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(deliveryPolicyService, times(0)).createDeliveryPolicy(any());
    }

    @Test
    @DisplayName("배송비 정책 수정 - 성공")
    void testUpdateDeliveryPolicy_Success() throws Exception {

        DeliveryPolicyUpdateRequestDto updateDto = new DeliveryPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyFee", 1000);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyCondition", 5000);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyState", true);


        String json = new ObjectMapper().writeValueAsString(updateDto);

        mockMvc.perform(put("/api/delivery-policies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        verify(deliveryPolicyService, times(1))
                .updateDeliveryPolicy(anyLong(), any());
    }

    @Test
    @DisplayName("배송비 정책 수정 - 실패(Validation)")
    void testUpdateDeliveryPolicy_Fail() throws Exception {

        DeliveryPolicyUpdateRequestDto updateDto = new DeliveryPolicyUpdateRequestDto();
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyFee", -1000);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyCondition", -5000);
        ReflectionTestUtils.setField(updateDto, "deliveryPolicyState", null);

        String json = new ObjectMapper().writeValueAsString(updateDto);

        mockMvc.perform(put("/api/delivery-policies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(deliveryPolicyService, times(0))
                .updateDeliveryPolicy(anyLong(), any());
    }
}