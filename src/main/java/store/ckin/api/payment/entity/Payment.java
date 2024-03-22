package store.ckin.api.payment.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.sale.entity.Sale;

/**
 * 결제 엔티티 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Getter
@Entity
@Table(name = "Payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @JoinColumn(name = "sale_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Sale sale;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "payment_status")
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "payment_approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "payment_receipt")
    private String receipt;


    @Builder
    public Payment(Long paymentId, Sale sale, String paymentKey,
                   PaymentStatus paymentStatus, LocalDateTime requestedAt,
                   LocalDateTime approvedAt, String receipt) {
        this.paymentId = paymentId;
        this.sale = sale;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.receipt = receipt;
    }

    public void cancelPayment() {
        this.paymentStatus = PaymentStatus.CANCEL;
    }
}
