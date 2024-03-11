package store.ckin.api.payment.exception;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */
public class PaymentAmountNotCorrectException extends RuntimeException {
    public PaymentAmountNotCorrectException() {
        super("Payment amount is not correct.");
    }
}
