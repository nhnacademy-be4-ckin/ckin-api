package store.ckin.api.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.review.dto.response.ReviewResponseDto;

/**
 * ReviewRepository Custom.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
public interface ReviewRepositoryCustom {
    Page<ReviewResponseDto> findReviewsByMemberWithPagination(Long memberId, Pageable pageable);

    Page<ReviewResponseDto> findReviewsByBookWithPagination(Long bookId, Pageable pageable);
}
