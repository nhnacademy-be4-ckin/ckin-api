package store.ckin.api.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.service.ReviewService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ReviewControllerTest
 *
 * @author : 이가은
 * @version : 2024. 03. 12
 */
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;
    private ReviewCreateRequestDto reviewCreateRequestDto;
    private ReviewResponseDto reviewResponseDto;

    @BeforeEach
    void setUp() {
        reviewCreateRequestDto = new ReviewCreateRequestDto();
        ReflectionTestUtils.setField(reviewCreateRequestDto, "memberId", 1L);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "bookId", 1L);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "reviewRate", 5);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "reviewComment", "인상 깊게 읽었습니다.");

        reviewResponseDto = ReviewResponseDto.builder()
                .reviewId(1L)
                .reviewDate("2023-03-12")
                .reviewRate(5)
                .message("good")
                .author("***un0000@email.com")
                .build();
    }

    @Test
    @DisplayName("리뷰 업로드 구현 테스트")
    void testPostReview() throws Exception {

        MockMultipartFile multipartFile1 = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile2 = new MockMultipartFile("file", "test2.txt", "text/plain", "test file2".getBytes(StandardCharsets.UTF_8));
        String json = objectMapper.writeValueAsString(reviewCreateRequestDto);
        MockMultipartFile createRequestDto = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", json.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/review")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .file(createRequestDto))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("리뷰 목록 반환 테스트")
    void testGetReviewPageList() throws Exception {
        Page<ReviewResponseDto> page = new PageImpl<>(List.of(reviewResponseDto));

        when(reviewService.getReviewPageList(any(), anyLong())).thenReturn(page);

        mockMvc.perform(get("/api/review/{bookId}", 1L)
                        .param("page", "0")
                        .param("size", "5")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId", is(reviewResponseDto.getReviewId()), Long.class))
                .andExpect(jsonPath("$.content[0].author", equalTo(reviewResponseDto.getAuthor())))
                .andExpect(jsonPath("$.content[0].message", equalTo(reviewResponseDto.getMessage())))
                .andExpect(jsonPath("$.content[0].reviewRate", equalTo(reviewResponseDto.getReviewRate())))
                .andExpect(jsonPath("$.content[0].reviewDate", equalTo(reviewResponseDto.getReviewDate())))
                .andDo(print());
    }
}