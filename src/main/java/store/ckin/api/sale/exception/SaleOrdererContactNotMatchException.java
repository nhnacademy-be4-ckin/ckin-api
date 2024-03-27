package store.ckin.api.sale.exception;

import store.ckin.api.advice.exception.GeneralForbiddenException;

/**
 * 주문 번호와 주문자 연락처가 일치하지 않을 때 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 14.
 */
public class SaleOrdererContactNotMatchException extends GeneralForbiddenException {

    public SaleOrdererContactNotMatchException() {
        super("주문 번호의 주문자 연락처가 일치하지 않습니다.");
    }
}
