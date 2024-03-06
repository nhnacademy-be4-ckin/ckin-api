package store.ckin.api.sale.exception;

/**
 * 주문 조회 실패 예외 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 05.
 */
public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException(Long saleId) {
        super(String.format("주문 ID %d에 해당하는 주문이 없습니다.", saleId));
    }
}
