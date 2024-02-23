package store.ckin.api.deliverypolicy.exception;

/**
 * 배송비 정책 Not Found 예외 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */
public class DeliveryPolicyNotFoundException extends RuntimeException {

    public DeliveryPolicyNotFoundException(Long id) {
        super(String.format("Delivery Policy Not Found [id = %d]", id));
    }
}
