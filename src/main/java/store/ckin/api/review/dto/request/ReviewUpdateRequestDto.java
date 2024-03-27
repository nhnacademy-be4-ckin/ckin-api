package store.ckin.api.review.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점을 초과할 수 없습니다.")
    @NotNull(message = "평점은 필수입니다.")
    private Integer reviewRate;

    @Size(max = 500, message = "리뷰 내용은 500자를 초과할 수 없습니다.")
    @NotNull(message = "리뷰 내용은 필수입니다.")
    private String reviewComment;
}

