package store.ckin.api.review.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.request.ReviewUpdateRequestDto;
import store.ckin.api.review.dto.response.MyPageReviewResponseDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.facade.ReviewFacade;

/**
 * ReviewControllerTest
 *
 * @author : 이가은
 * @version : 2024. 03. 12
 */
@AutoConfigureRestDocs
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewFacade reviewFacade;
    private ReviewCreateRequestDto reviewCreateRequestDto;
    private ReviewResponseDto reviewResponseDto;
    private MyPageReviewResponseDto myPageReviewResponseDto;
    private ReviewUpdateRequestDto reviewUpdateRequestDto;

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

        myPageReviewResponseDto = MyPageReviewResponseDto.builder()
                .reviewId(1L)
                .reviewDate("2023-03-12")
                .reviewRate(4)
                .message("good")
                .author("***un0000@email.com")
                .thumbnailPath("url")
                .bookId(1L)
                .bookTitle("어린왕자")
                .filePath(List.of("url1", "url2"))
                .build();

        reviewUpdateRequestDto = new ReviewUpdateRequestDto();
        ReflectionTestUtils.setField(reviewUpdateRequestDto, "reviewId", 1L);
        ReflectionTestUtils.setField(reviewUpdateRequestDto, "reviewRate", 5);
        ReflectionTestUtils.setField(reviewUpdateRequestDto, "reviewComment", "인상깊은 책입니다. ");

    }

    @Test
    @DisplayName("리뷰 업로드 구현 테스트")
    void testPostReview() throws Exception {

        MockMultipartFile multipartFile1 =
                new MockMultipartFile("image1", "test.png", "text/plain", "test file".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile multipartFile2 =
                new MockMultipartFile("image2", "test2.png", "text/plain", "test file2".getBytes(StandardCharsets.UTF_8));
        String json = objectMapper.writeValueAsString(reviewCreateRequestDto);
        MockMultipartFile createRequestDto =
                new MockMultipartFile("createRequestDto", "createRequestDto", "application/json",
                        json.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/review")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .file(createRequestDto)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(document("review/postReview/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image1").description("이미지 파일"),
                                partWithName("image2").description("이미지 파일"),
                                partWithName("createRequestDto").description("리뷰 생성 DTO")
                        ),
                        requestPartFields("createRequestDto",
                                fieldWithPath("memberId").description("회원 아이디"),
                                fieldWithPath("bookId").description("도서 아이디"),
                                fieldWithPath("reviewRate").description("리뷰 점수"),
                                fieldWithPath("reviewComment").description("리뷰 내용")
                        )));
    }

    @Test
    @DisplayName("리뷰 목록 반환 테스트")
    void testGetReviewPageList() throws Exception {
        Page<ReviewResponseDto> page = new PageImpl<>(List.of(reviewResponseDto));

        when(reviewFacade.getReviewPageList(any(), anyLong())).thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/review/{bookId}", 1L)
                        .param("page", "0")
                        .param("size", "5")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId", is(reviewResponseDto.getReviewId()), Long.class))
                .andExpect(jsonPath("$.content[0].author", equalTo(reviewResponseDto.getAuthor())))
                .andExpect(jsonPath("$.content[0].message", equalTo(reviewResponseDto.getMessage())))
                .andExpect(jsonPath("$.content[0].reviewRate", equalTo(reviewResponseDto.getReviewRate())))
                .andExpect(jsonPath("$.content[0].reviewDate", equalTo(reviewResponseDto.getReviewDate())))
                .andDo(document("review/getReviewPageList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("지정할 페이지"),
                                parameterWithName("size").description("한 페이지 당 표시할 개수")
                        ),
                        pathParameters(
                                parameterWithName("bookId").description("도서 아이디")
                        ),
                        responseFields(
                                fieldWithPath("content.[].reviewId").description("리뷰 아이디"),
                                fieldWithPath("content.[].author").description("리뷰 작성자 이름"),
                                fieldWithPath("content.[].message").description("리뷰 내용"),
                                fieldWithPath("content.[].reviewRate").description("리뷰 점수"),
                                fieldWithPath("content.[].reviewDate").description("리뷰 작성 날짜"),
                                fieldWithPath("content.[].filePath").description("리뷰 사진 주소"),
                                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("totalElements").description("전체 요소 개수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("sort.empty").description("정렬된 요소가 비어있는지 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 요소가 있는지 여부"),
                                fieldWithPath("sort.sorted").description("정렬된 요소가 있는지 여부"),
                                fieldWithPath("first").description("현재 페이지의 첫 번째 요소 여부"),
                                fieldWithPath("size").description("현재 페이지의 요소 개수"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 개수"),
                                fieldWithPath("empty").description("현재 페이지의 요소가 비어있는지 여부"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("pageable").description("현재 페이지 정보")
                        )));
    }

    @Test
    @DisplayName("리뷰 목록 반환 테스트: 마이페이지")
    void testGetReviewPageListByMemberId() throws Exception {
        Page<MyPageReviewResponseDto> page = new PageImpl<>(List.of(myPageReviewResponseDto), PageRequest.of(0, 5), 1);

        when(reviewFacade.findReviewsByMemberWithPagination(anyLong(), any())).thenReturn(page);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/review/my-page/{memberId}", 1L)
                        .param("page", "0")
                        .param("size", "5")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId", is(myPageReviewResponseDto.getReviewId()), Long.class))
                .andExpect(jsonPath("$.content[0].author", equalTo(myPageReviewResponseDto.getAuthor())))
                .andExpect(jsonPath("$.content[0].message", equalTo(myPageReviewResponseDto.getMessage())))
                .andExpect(jsonPath("$.content[0].reviewRate", equalTo(myPageReviewResponseDto.getReviewRate())))
                .andExpect(jsonPath("$.content[0].reviewDate", equalTo(myPageReviewResponseDto.getReviewDate())))
                .andExpect(jsonPath("$.content[0].thumbnailPath", equalTo(myPageReviewResponseDto.getThumbnailPath())))
                .andExpect(jsonPath("$.content[0].bookId", is(myPageReviewResponseDto.getBookId()), Long.class))
                .andExpect(jsonPath("$.content[0].bookTitle", equalTo(myPageReviewResponseDto.getBookTitle())))
                .andDo(document("review/getReviewPageListByMemberId/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("지정할 페이지"),
                                parameterWithName("size").description("한 페이지 당 표시할 개수")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("회원 아이디")
                        ),
                        responseFields(
                                fieldWithPath("content.[].reviewId").description("리뷰 아이디"),
                                fieldWithPath("content.[].author").description("리뷰 작성자 이름"),
                                fieldWithPath("content.[].message").description("리뷰 내용"),
                                fieldWithPath("content.[].reviewRate").description("리뷰 점수"),
                                fieldWithPath("content.[].reviewDate").description("리뷰 작성 날짜"),
                                fieldWithPath("content.[].thumbnailPath").description("썸네일 경로"),
                                fieldWithPath("content.[].bookId").description("도서 아이디"),
                                fieldWithPath("content.[].bookTitle").description("도서 제목"),
                                fieldWithPath("content.[].filePath").description("리뷰 사진 주소"),
                                fieldWithPath("pageable.offset").description("페이지의 시작 오프셋"),
                                fieldWithPath("pageable.pageNumber").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("pageable.pageSize").description("페이지당 요소 개수"),
                                fieldWithPath("pageable.paged").description("페이징된 결과인지 여부"),
                                fieldWithPath("pageable.unpaged").description("페이징되지 않은 결과인지 여부"),
                                fieldWithPath("pageable.sort.empty").description("정렬된 요소가 비어있는지 여부"),
                                fieldWithPath("pageable.sort.sorted").description("정렬된 요소가 있는지 여부"),
                                fieldWithPath("pageable.sort.unsorted").description("정렬되지 않은 요소가 있는지 여부"),
                                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("totalElements").description("전체 요소 개수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("sort.empty").description("정렬된 요소가 비어있는지 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 요소가 있는지 여부"),
                                fieldWithPath("sort.sorted").description("정렬된 요소가 있는지 여부"),
                                fieldWithPath("first").description("현재 페이지의 첫 번째 요소 여부"),
                                fieldWithPath("size").description("현재 페이지의 요소 개수"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 개수"),
                                fieldWithPath("empty").description("현재 페이지의 요소가 비어있는지 여부"),
                                fieldWithPath("number").description("현재 페이지 번호")
                        )));
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void testUpdateReview() throws Exception {
        mockMvc.perform(put("/api/members/review/{memberId}", 1L)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("review/updateReview/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("reviewId").description("수정할 리뷰 아이디"),
                                fieldWithPath("reviewRate").description("수정할 리뷰 점수"),
                                fieldWithPath("reviewComment").description("수정할 리뷰 내용")
                        )));
    }


}