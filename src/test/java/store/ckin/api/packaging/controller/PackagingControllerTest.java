package store.ckin.api.packaging.controller;

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
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.dto.request.PackagingUpdateRequestDto;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.service.PackagingService;

/**
 * 포장 정책 컨트롤러 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */
@WebMvcTest(PackagingController.class)
class PackagingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PackagingService packagingService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("포장 정책 생성 - 성공")
    void testCreatePackagingPolicy_Success() throws Exception {

        PackagingCreateRequestDto requestDto = new PackagingCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingType", "생일선물 포장");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", 5000);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/packaging")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(packagingService, times(1)).createPackagingPolicy(any());
    }

    @Test
    @DisplayName("포장 정책 생성 - 실패 (Validation)")
    void testCreatePackagingPolicy_Fail() throws Exception {

        PackagingCreateRequestDto requestDto = new PackagingCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingType", "");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", -1000);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/packaging")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(packagingService, times(0)).createPackagingPolicy(any());
    }

    @Test
    @DisplayName("포장 정책 조회")
    void testGetPackagingPolicy() throws Exception {

        PackagingResponseDto responseDto = new PackagingResponseDto(1L, "생일선물 포장", 5000);

        given(packagingService.getPackagingPolicy(anyLong()))
                .willReturn(responseDto);

        mockMvc.perform(get("/api/packaging/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.packagingId").value(responseDto.getPackagingId()))
                .andExpect(jsonPath("$.packagingType").value(responseDto.getPackagingType()))
                .andExpect(jsonPath("$.packagingPrice").value(responseDto.getPackagingPrice()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(packagingService, times(1)).getPackagingPolicy(anyLong());
    }

    @Test
    @DisplayName("포장 정책 전체 조회")
    void testGetPackagingPolicies() throws Exception {

        List<PackagingResponseDto> responseDto = List.of(
                new PackagingResponseDto(1L, "꽃 생일선물 포장", 5000),
                new PackagingResponseDto(2L, "가죽 생일선물 포장", 10000)
        );


        given(packagingService.getPackagingPolicies())
                .willReturn(responseDto);

        mockMvc.perform(get("/api/packaging"))
                .andExpect(jsonPath("$[0].packagingId").value(responseDto.get(0).getPackagingId()))
                .andExpect(jsonPath("$[0].packagingType").value(responseDto.get(0).getPackagingType()))
                .andExpect(jsonPath("$[0].packagingPrice").value(responseDto.get(0).getPackagingPrice()))
                .andExpect(jsonPath("$[1].packagingId").value(responseDto.get(1).getPackagingId()))
                .andExpect(jsonPath("$[1].packagingType").value(responseDto.get(1).getPackagingType()))
                .andExpect(jsonPath("$[1].packagingPrice").value(responseDto.get(1).getPackagingPrice()))
                .andDo(print());

        verify(packagingService, times(1)).getPackagingPolicies();
    }

    @Test
    @DisplayName("포장 정책 수정 - 성공")
    void testUpdatePackagingPolicy_Success() throws Exception {

        PackagingUpdateRequestDto requestDto = new PackagingUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingId", 1L);
        ReflectionTestUtils.setField(requestDto, "packagingType", "생일선물 포장");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", 5000);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/packaging")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        verify(packagingService, times(1)).updatePackagingPolicy(any());
    }

    @Test
    @DisplayName("포장 정책 수정 - 실패 (Validation)")
    void testUpdatePackagingPolicy_Fail() throws Exception {

        PackagingUpdateRequestDto requestDto = new PackagingUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "packagingId", null);
        ReflectionTestUtils.setField(requestDto, "packagingType", "");
        ReflectionTestUtils.setField(requestDto, "packagingPrice", -1000);

        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/packaging")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(packagingService, times(0)).updatePackagingPolicy(any());
    }

    @Test
    @DisplayName("포장 정책 삭제")
    void testDeletePackagingPolicy() throws Exception {

        mockMvc.perform(delete("/api/packaging/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print());

        verify(packagingService, times(1)).deletePackagingPolicy(anyLong());
    }
}