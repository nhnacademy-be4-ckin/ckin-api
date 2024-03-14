package store.ckin.api.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.payment.entity.Payment;

/**
 * 결제 레포지토리 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {

}
