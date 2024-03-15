package store.ckin.api.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.review.dto.response.MyPageReviewResponseDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;

/**
 * ReviewRepository Custom.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
@NoRepositoryBean
public interface ReviewRepositoryCustom {
    Page<MyPageReviewResponseDto> findReviewsByMemberWithPagination(Long memberId, Pageable pageable);

    Page<ReviewResponseDto> getReviewPageList(Pageable pageable, Long bookId);
}
