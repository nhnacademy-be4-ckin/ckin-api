package store.ckin.api.review.dto.request;

import lombok.Getter;

/**
 * ReviewCreateRequestDto 리뷰 등록 요청 dto.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
@Getter
public class ReviewCreateRequestDto {

    private Long memberId;
    private Long bookId;
    private Integer reviewRate;
    private String reviewComment;

}
