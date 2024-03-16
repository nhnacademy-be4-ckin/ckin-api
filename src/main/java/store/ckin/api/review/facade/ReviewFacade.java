package store.ckin.api.review.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.service.PointHistoryService;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.service.PointPolicyService;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.service.ReviewService;

/**
 * 리뷰 퍼사드 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewService reviewService;

    private final PointPolicyService pointPolicyService;

    private final PointHistoryService pointHistoryService;

    private final MemberService memberService;

    private static final Long REVIEW_POINT_POLICY_ID = 200L;

    private static final Long REVIEW_WITH_PHOTO_POINT_POLICY_ID = 201L;

    @Transactional
    public void postReview(ReviewCreateRequestDto createRequestDto, List<MultipartFile> imageList) {

        reviewService.postReview(createRequestDto, imageList);

        Long pointPolicyId = Objects.isNull(imageList) ? REVIEW_POINT_POLICY_ID : REVIEW_WITH_PHOTO_POINT_POLICY_ID;
        PointPolicyResponseDto pointPolicy = pointPolicyService.getPointPolicy(pointPolicyId);

        PointHistoryCreateRequestDto pointHistory = PointHistoryCreateRequestDto.builder()
                .memberId(createRequestDto.getMemberId())
                .pointHistoryPoint(pointPolicy.getPointPolicyReserve())
                .pointHistoryReason(pointPolicy.getPointPolicyName())
                .pointHistoryTime(LocalDate.now())
                .build();

        log.info("pointHistory = {}", pointHistory);

        pointHistoryService.createPointHistory(pointHistory);
        memberService.updatePoint(createRequestDto.getMemberId(), pointPolicy.getPointPolicyReserve());
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviewPageList(Pageable pageable, Long bookId) {
        return reviewService.getReviewPageList(pageable, bookId);
    }
}
