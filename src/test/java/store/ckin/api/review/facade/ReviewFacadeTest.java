package store.ckin.api.review.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.pointhistory.service.PointHistoryService;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.service.PointPolicyService;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.service.ReviewService;

/**
 * 리뷰 퍼사드 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 03. 16.
 */
@ExtendWith(MockitoExtension.class)
class ReviewFacadeTest {

    @InjectMocks
    ReviewFacade reviewFacade;

    @Mock
    ReviewService reviewService;

    @Mock
    PointPolicyService pointPolicyService;

    @Mock
    PointHistoryService pointHistoryService;

    @Mock
    MemberService memberService;


    ReviewCreateRequestDto reviewCreateRequestDto;

    List<MultipartFile> imageList;

    @BeforeEach
    void setUp() {

        reviewCreateRequestDto = new ReviewCreateRequestDto();
        ReflectionTestUtils.setField(reviewCreateRequestDto, "memberId", 1L);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "bookId", 1L);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "reviewRate", 5);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "reviewComment", "인상 깊게 읽었습니다.");

        imageList = new ArrayList<>();
    }

    @Test
    @DisplayName("리뷰 생성 테스트")
    void testPostReview() {

        PointPolicyResponseDto pointPolicy = new PointPolicyResponseDto(1L, "리뷰 작성", 200);

        given(pointPolicyService.getPointPolicy(anyLong()))
                .willReturn(pointPolicy);

        reviewFacade.postReview(reviewCreateRequestDto, imageList);

        verify(reviewService, times(1)).postReview(any(), any());
    }

    @Test
    @DisplayName("리뷰 페이지 리스트 조회 테스트")
    void testGetReviewPageList() {

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(1L, "***un0000@email.com", "good", 5, "2023-03-12");

        PageImpl<ReviewResponseDto> reviewPage = new PageImpl<>(List.of(reviewResponseDto));

        given(reviewService.getReviewPageList(any(), anyLong()))
                .willReturn(reviewPage);

        Page<ReviewResponseDto> reviewPageList =
                reviewService.getReviewPageList(Pageable.ofSize(10), 1L);

        List<ReviewResponseDto> content = reviewPage.getContent();
        assertAll(
                () -> assertEquals(0, reviewPageList.getNumber()),
                () -> assertEquals(1, reviewPageList.getSize()),
                () -> assertEquals(1, reviewPageList.getTotalElements()),
                () -> assertEquals(1, reviewPageList.getTotalPages()),
                () -> assertEquals(1, content.size()),
                () -> assertEquals(reviewResponseDto, content.get(0))
        );

        verify(reviewService, times(1)).getReviewPageList(any(), anyLong());
    }
}