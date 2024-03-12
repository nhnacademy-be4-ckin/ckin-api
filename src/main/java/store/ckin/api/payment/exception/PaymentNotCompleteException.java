package store.ckin.api.payment.exception;

/**
 * 결제가 완료되지 않은 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */
public class PaymentNotCompleteException extends RuntimeException{

    public PaymentNotCompleteException() {
        super("Payment is not complete.");
    }
}
