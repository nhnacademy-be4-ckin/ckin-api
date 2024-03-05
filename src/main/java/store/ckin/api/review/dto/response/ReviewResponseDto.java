package store.ckin.api.review.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ReviewResponseDto 리뷰 응답 dto.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewComment;
    private LocalDateTime createdAt;
    private String reviewWriter;

    @Builder
    public ReviewResponseDto(Long reviewId, String reviewComment, LocalDateTime createdAt, String reviewWriter) {
        this.reviewId = reviewId;
        this.reviewComment = reviewComment;
        this.createdAt = createdAt;
        this.reviewWriter = reviewWriter;
    }
}
