package store.ckin.api.review.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ReviewCreateRequestDto 리뷰 등록 요청 dto.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
@Getter
@NoArgsConstructor
public class ReviewCreateRequestDto {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "책 ID는 필수입니다.")
    private Long bookId;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점을 초과할 수 없습니다.")
    private Integer reviewRate;

    @NotNull(message = "리뷰 내용은 필수입니다.")
    @Size(max = 500, message = "리뷰 내용은 500자를 초과할 수 없습니다.")
    private String reviewComment;

}
