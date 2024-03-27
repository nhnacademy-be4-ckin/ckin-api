package store.ckin.api.sale.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 주문번호로 조회된 주문이 없는 경우에 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

public class SaleNumberNotFoundException extends GeneralNotFoundException {

    public SaleNumberNotFoundException() {
        super("주문 정보가 존재하지 않습니다.");
    }
}
