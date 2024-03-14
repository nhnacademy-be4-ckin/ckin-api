package store.ckin.api.sale.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.payment.dto.response.PaymentResponseDto;

/**
 * 주문 상세 조회 응답 DTO 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */

@Getter
@AllArgsConstructor
public class SaleDetailResponseDto {

    private List<BookAndBookSaleResponseDto> bookSaleList;

    private SaleResponseDto saleResponseDto;

    private PaymentResponseDto paymentResponseDto;
}
