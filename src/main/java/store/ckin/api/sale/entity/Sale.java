package store.ckin.api.sale.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.member.entity.Member;

/**
 * 주문 엔티티 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 26.
 */

@Getter
@Entity
@Table(name = "Sale")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sale {


    /**
     * 주문의 배송 상태를 나타내는 Enum.
     */
    public enum DeliveryStatus {
        READY, IN_PROGRESS, DONE
    }

    /**
     * 주문의 결제 상태를 나타내는 Enum.
     */
    public enum PaymentStatus {
        WAITING, PAID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
    private Set<BookSale> bookSales;

    @Column(name = "sale_title")
    private String saleTitle;

    @Column(name = "sale_number")
    private String saleNumber;

    @Column(name = "sale_orderer_name")
    private String saleOrdererName;

    @Column(name = "sale_orderer_contact")
    private String saleOrdererContact;

    @Column(name = "sale_receiver_name")
    private String saleReceiverName;

    @Column(name = "sale_receiver_contact")
    private String saleReceiverContact;

    @Column(name = "sale_receiver_address")
    private String saleReceiverAddress;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Column(name = "sale_shipping_date")
    private LocalDateTime saleShippingDate;

    @Column(name = "sale_delivery_date")
    private LocalDate saleDeliveryDate;

    @Column(name = "sale_delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus saleDeliveryStatus;

    @Column(name = "sale_delivery_fee")
    private Integer saleDeliveryFee;

    @Column(name = "sale_point_usage")
    private Integer salePointUsage;

    @Column(name = "sale_total_price")
    private Integer saleTotalPrice;

    @Column(name = "sale_payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus salePaymentStatus;

    @Column(name = "sale_shipping_post_code")
    private String saleShippingPostCode;

    @Builder
    public Sale(Long saleId, Member member, Set<BookSale> bookSales, String saleTitle, String saleNumber,
                String saleOrdererName, String saleOrdererContact, String saleReceiverName, String saleReceiverContact,
                String saleReceiverAddress, LocalDateTime saleDate, LocalDateTime saleShippingDate,
                LocalDate saleDeliveryDate, DeliveryStatus saleDeliveryStatus, Integer saleDeliveryFee,
                Integer salePointUsage, Integer saleTotalPrice, PaymentStatus salePaymentStatus,
                String saleShippingPostCode) {
        this.saleId = saleId;
        this.member = member;
        this.bookSales = bookSales;
        this.saleTitle = saleTitle;
        this.saleNumber = saleNumber;
        this.saleOrdererName = saleOrdererName;
        this.saleOrdererContact = saleOrdererContact;
        this.saleReceiverName = saleReceiverName;
        this.saleReceiverContact = saleReceiverContact;
        this.saleReceiverAddress = saleReceiverAddress;
        this.saleDate = saleDate;
        this.saleShippingDate = saleShippingDate;
        this.saleDeliveryDate = saleDeliveryDate;
        this.saleDeliveryStatus = saleDeliveryStatus;
        this.saleDeliveryFee = saleDeliveryFee;
        this.salePointUsage = salePointUsage;
        this.saleTotalPrice = saleTotalPrice;
        this.salePaymentStatus = salePaymentStatus;
        this.saleShippingPostCode = saleShippingPostCode;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.salePaymentStatus = paymentStatus;
    }
}
