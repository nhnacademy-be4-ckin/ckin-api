package store.ckin.api.payment.exception;

import store.ckin.api.advice.exception.GeneralBadRequestException;

/**
 * 결제 금액이 올바르지 않을 때 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */
public class PaymentAmountNotCorrectException extends GeneralBadRequestException {

    public PaymentAmountNotCorrectException(Integer saleTotalPrice, Integer requestTotalPrice) {
        super(String.format("결제 금액이 올바르지 않습니다. [saleTotalPrice = %d, requestTotalPrice = %d]",
                saleTotalPrice, requestTotalPrice));
    }
}
