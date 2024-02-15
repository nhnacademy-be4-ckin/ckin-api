package store.ckin.api.deliverypolicy.exception;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */
public class DeliveryPolicyNotFoundException extends RuntimeException {

    public DeliveryPolicyNotFoundException(Long id) {
        super("Delivery Policy Not Found [id = " + id + "]");
    }
}
