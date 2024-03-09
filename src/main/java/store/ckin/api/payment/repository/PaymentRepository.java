package store.ckin.api.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.payment.entity.Payment;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
