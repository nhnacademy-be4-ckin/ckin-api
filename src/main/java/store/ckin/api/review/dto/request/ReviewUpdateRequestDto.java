package store.ckin.api.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ReviewUpdateRequestDto.
 *
 * @author 나국로
 * @version 2024. 03. 19.
 */
@Getter
@NoArgsConstructor
public class ReviewUpdateRequestDto {
    private Long reviewId;
    private Integer reviewRate;
    private String reviewComment;
}
