package store.ckin.api.sale.exception;

import store.ckin.api.advice.exception.GeneralForbiddenException;

/**
 * 주문자 정보와 회원 아이디가 일치하지 않을 때 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
public class SaleMemberNotMatchException extends GeneralForbiddenException {

    public SaleMemberNotMatchException(String saleNumber) {
        super(String.format("주문 번호 (%s)의 주문자 ID와 요청한 회원 ID가 일치하지 않습니다.", saleNumber));
    }
}
