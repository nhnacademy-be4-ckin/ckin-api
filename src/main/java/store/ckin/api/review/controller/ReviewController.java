package store.ckin.api.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.service.ReviewService;

import java.util.List;

/**
 * ReviewController 클래스.
 *
 * @author 이가은
 * @version 2024. 03. 11.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 업로드를 구현하는 메소드 입니다.
     *
     * @param createRequestDto 도서 아이디, 리뷰 점수, 리뷰 코멘트를 담고 있는 DTO 입니다.
     * @param imageList 리뷰의 이미지 리스트를 담고 있는 MultipartFile 리스트 입니다.
     */
    @PostMapping
    public void postReview(@RequestPart ReviewCreateRequestDto createRequestDto,
                           @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList) {

        reviewService.postReview(createRequestDto, imageList);
    }
}
