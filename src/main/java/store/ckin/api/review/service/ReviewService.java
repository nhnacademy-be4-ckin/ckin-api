package store.ckin.api.review.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.request.ReviewUpdateRequestDto;
import store.ckin.api.review.dto.response.MyPageReviewResponseDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;

/**
 * ReviewService
 *
 * @author 이가은
 * @version 2024. 03. 11.
 */
public interface ReviewService {

    /**
     * 리뷰 업로드를 구현하는 메소드 입니다.
     *
     * @param createRequestDto 도서 아이디, 리뷰 점수, 리뷰 코멘트를 담고 있는 DTO 입니다.
     * @param imageList        리뷰의 이미지 리스트를 담고 있는 MultipartFile 리스트 입니다.
     */
    void postReview(ReviewCreateRequestDto createRequestDto, List<MultipartFile> imageList);

    /**
     * 도서 아이디로 해당되는 리뷰 목록을 반환하는 메소드 입니다.
     *
     * @param pageable 리뷰 페이지
     * @param bookId   도서 아이디
     * @return 리뷰 DTO 페이지
     */
    Page<ReviewResponseDto> getReviewPageList(Pageable pageable, Long bookId);

    Page<MyPageReviewResponseDto> findReviewsByMemberWithPagination(Long memberId, Pageable pageable);

    void updateReview(ReviewUpdateRequestDto updateRequestDto, Long memberId);
}
