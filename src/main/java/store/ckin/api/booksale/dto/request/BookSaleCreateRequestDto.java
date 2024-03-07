package store.ckin.api.booksale.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 주문 도서 생성 요청 DTO.
 *
 * @version 2024. 03. 02.
 */

@ToString
@Getter
@NoArgsConstructor
public class BookSaleCreateRequestDto {


    @Positive(message = "유효한 도서 아이디를 입력해주세요.")
    @NotNull(message = "도서 아이디가 입력되지 않았습니다")
    private Long bookId;

    private Long appliedCouponId;

    private Long packagingId;

    @Positive(message = "도서의 수량은 1보다 작을 수 없습니다.")
    @NotNull(message = "도서의 수량이 입력되지 않았습니다")
    private Integer quantity;

    @Positive(message = "도서의 결제 금액은 1보다 작을 수 없습니다.")
    @NotNull(message = "도서의 결제 금액이 입력되지 않았습니다")
    private Integer paymentAmount;
}
