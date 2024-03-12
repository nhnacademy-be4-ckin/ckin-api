package store.ckin.api.payment.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.entity.Payment;
import store.ckin.api.payment.entity.QPayment;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */

public class PaymentRepositoryImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

    @Override
    public PaymentResponseDto getPaymentBySaleId(Long saleId) {

        QPayment payment = QPayment.payment;

        return from(payment)
                .where(payment.sale.saleId.eq(saleId))
                .select(Projections.constructor(PaymentResponseDto.class,
                        payment.paymentId,
                        payment.sale.saleId,
                        payment.paymentKey,
                        payment.paymentStatus,
                        payment.requestedAt,
                        payment.approvedAt,
                        payment.receipt))
                .fetchOne();
    }
}
