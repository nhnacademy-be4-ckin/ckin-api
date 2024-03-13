package store.ckin.api.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String author;
    private String message;
    private Integer reviewRate;
    private String reviewDate;
    private List<String> filePath;

    @Builder
    public ReviewResponseDto(Long reviewId, String author, String message, Integer reviewRate, String reviewDate) {
        this.reviewId = reviewId;
        this.author = author;
        this.message = message;
        this.reviewRate = reviewRate;
        this.reviewDate = reviewDate;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }
}
