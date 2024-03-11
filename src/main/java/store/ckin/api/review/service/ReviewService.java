package store.ckin.api.review.service;

import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;

import java.util.List;

/**
 * ReviewService
 *
 * @author 이가은
 * @version 2024. 03. 11.
 */
public interface ReviewService {
    void postReview(ReviewCreateRequestDto createRequestDto, List<MultipartFile> imageList);
}
