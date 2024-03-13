package store.ckin.api.payment.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.sale.entity.Sale;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String paymentStatus;

    @Column(name = "payment_requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "payment_approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "payment_receipt")
    private String receipt;


    @Builder
    public Payment(Long paymentId, Sale sale, String paymentKey, String paymentStatus, LocalDateTime requestedAt,
                   LocalDateTime approvedAt, String receipt) {
        this.paymentId = paymentId;
        this.sale = sale;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.receipt = receipt;
    }
}
