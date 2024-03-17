package store.ckin.api.pointhistory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;
import store.ckin.api.pointhistory.service.PointHistoryService;

/**
 * 포인트 내역 컨트롤러 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 16.
 */

@WebMvcTest(PointHistoryController.class)
class PointHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PointHistoryService pointHistoryService;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("포인트 내역 생성 테스트")
    void testCreatePointHistory() throws Exception {

        PointHistoryCreateRequestDto pointHistory = PointHistoryCreateRequestDto.builder()
                .memberId(1L)
                .pointHistoryReason("회원가입")
                .pointHistoryPoint(5000)
                .pointHistoryTime(LocalDate.now())
                .build();

        String json = objectMapper.writeValueAsString(pointHistory);

        mockMvc.perform(post("/api/point-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("포인트 내역 조회 테스트")
    void testGetPointHistoryList() throws Exception {
        PointHistoryResponseDto pointHistory
                = new PointHistoryResponseDto(1L, 1L, "회원가입", 5000, LocalDate.now());

        List<PointHistoryResponseDto> pointHistoryList = List.of(pointHistory);

        PageInfo pageInfo = PageInfo.builder()
                .page(0)
                .size(10)
                .totalPages(1)
                .totalElements(1)
                .build();

        PagedResponse<List<PointHistoryResponseDto>> pagedResponse
                = new PagedResponse<>(pointHistoryList, pageInfo);

        given(pointHistoryService.getPointHistoryList(anyLong(), any()))
                .willReturn(pagedResponse);


        mockMvc.perform(get("/api/point-history")
                        .param("memberId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpectAll(
                        jsonPath("$.data[0].id").value(pointHistory.getId()),
                        jsonPath("$.data[0].memberId").value(pointHistory.getMemberId()),
                        jsonPath("$.data[0].pointHistoryReason").value(pointHistory.getPointHistoryReason()),
                        jsonPath("$.data[0].pointHistoryPoint").value(pointHistory.getPointHistoryPoint()),
                        jsonPath("$.data[0].pointHistoryTime").value(pointHistory.getPointHistoryTime().toString()),
                        jsonPath("$.pageInfo.page").value(pageInfo.getPage()),
                        jsonPath("$.pageInfo.size").value(pageInfo.getSize()),
                        jsonPath("$.pageInfo.totalPages").value(pageInfo.getTotalPages()),
                        jsonPath("$.pageInfo.totalElements").value(pageInfo.getTotalElements()))
                .andDo(print());
    }
}