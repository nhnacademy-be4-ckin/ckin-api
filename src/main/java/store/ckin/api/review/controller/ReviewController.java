package store.ckin.api.review.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.request.ReviewUpdateRequestDto;
import store.ckin.api.review.dto.response.MyPageReviewResponseDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.facade.ReviewFacade;

/**
 * ReviewController 클래스.
 *
 * @author 이가은
 * @version 2024. 03. 11.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewFacade reviewFacade;


    /**
     * 리뷰 업로드를 구현하는 메소드 입니다.
     *
     * @param createRequestDto 도서 아이디, 리뷰 점수, 리뷰 코멘트를 담고 있는 DTO 입니다.
     * @param imageList        리뷰의 이미지 리스트를 담고 있는 MultipartFile 리스트 입니다.
     */
    @PostMapping("/review")
    public ResponseEntity<Void> postReview(@RequestPart @Valid ReviewCreateRequestDto createRequestDto,
                                           @RequestPart(value = "imageList", required = false)
                                           List<MultipartFile> imageList) {

        reviewFacade.postReview(createRequestDto, imageList);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 도서 아이디로 해당되는 리뷰 목록을 반환하는 메소드 입니다.
     *
     * @param pageable 리뷰 페이지
     * @param bookId   도서 아이디
     * @return 리뷰 DTO 페이지
     */
    @GetMapping("/review/{bookId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewPageList(
            @PageableDefault(size = 5) Pageable pageable,
            @PathVariable("bookId") Long bookId) {
        Page<ReviewResponseDto> content = reviewFacade.getReviewPageList(pageable, bookId);

        return ResponseEntity.ok().body(content);
    }

    /**
     * 회원 아이디로 해당되는 리뷰 목록을 반환하는 메소드 입니다.
     *
     * @param pageable 리뷰 페이지
     * @param memberId 회원 아이디
     * @return 리뷰 DTO 페이지
     */
    @GetMapping("/members/review/my-page/{memberId}")
    public ResponseEntity<Page<MyPageReviewResponseDto>> getReviewPageListByMemberId(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @PathVariable("memberId") Long memberId) {
        Page<MyPageReviewResponseDto> content = reviewFacade.findReviewsByMemberWithPagination(memberId, pageable);

        return ResponseEntity.ok().body(content);
    }

    /**
     * 마이페이지에서 리뷰를 수정하기 위한 메소드 입니다.
     *
     * @param updateRequestDto 수정을 위한 DTO
     * @param memberId         회원 아이디
     */
    @PutMapping("/members/review/{memberId}")
    public ResponseEntity<Void> updateReview(@RequestBody @Valid ReviewUpdateRequestDto updateRequestDto,
                                             @PathVariable Long memberId) {

        reviewFacade.updateReview(updateRequestDto, memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
