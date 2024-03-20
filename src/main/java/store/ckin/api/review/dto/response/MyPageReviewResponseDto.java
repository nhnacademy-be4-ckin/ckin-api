package store.ckin.api.review.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MyPageReviewResponseDto.
 *
 * @author 나국로
 * @version 2024. 03. 14.
 */
@Getter
@NoArgsConstructor
public class MyPageReviewResponseDto {
    private Long reviewId;
    private String author;
    private String message;
    private Integer reviewRate;
    private String reviewDate;
    private String thumbnailPath;
    private Long bookId;
    private String bookTitle;
    private List<String> filePath;

    @Builder
    public MyPageReviewResponseDto(Long reviewId, String author, String message, Integer reviewRate, String reviewDate,
                                   List<String> filePath, String thumbnailPath, Long bookId, String bookTitle) {
        this.reviewId = reviewId;
        this.author = author;
        this.message = message;
        this.reviewRate = reviewRate;
        this.reviewDate = reviewDate;
        this.filePath = filePath;
        this.thumbnailPath = thumbnailPath;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
