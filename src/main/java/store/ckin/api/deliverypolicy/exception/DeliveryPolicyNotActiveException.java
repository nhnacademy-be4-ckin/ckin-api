package store.ckin.api.deliverypolicy.exception;

/**
 * 배송 정책이 활성화되지 않은 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 27.
 */
public class DeliveryPolicyNotActiveException extends RuntimeException {

    public DeliveryPolicyNotActiveException() {
        super("Delivery Policy is not active!");
    }
}