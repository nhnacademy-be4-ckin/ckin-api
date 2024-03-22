package store.ckin.api.sale.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 주문 조회 실패 예외 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 05.
 */
public class SaleNotFoundException extends GeneralNotFoundException {
    public SaleNotFoundException(Long saleId) {
        super(String.format("주문 ID %d에 해당하는 주문이 없습니다.", saleId));
    }
}
