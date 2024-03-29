package store.ckin.api.deliverypolicy.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 배송비 정책 Not Found 예외 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */
public class DeliveryPolicyNotFoundException extends GeneralNotFoundException {

    public DeliveryPolicyNotFoundException() {
        super("배송비 정책이 존재하지 않습니다.");
    }
}
