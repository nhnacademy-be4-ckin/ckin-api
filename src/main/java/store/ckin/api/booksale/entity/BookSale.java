package store.ckin.api.booksale.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 리스트 Entity.
 *
 * @author 정승조
 * @version 2024. 02. 27.
 */

@Entity
@Table(name = "BookSale")
public class BookSale {

    public enum BookSaleState {
        ORDER, RETURN, CANCEL, COMPLETE
    }

    @EmbeddedId
    private Pk pk;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "booksale_quantity")
    private Integer bookSaleQuantity;

    @Column(name = "booksale_packaging_price")
    private Integer bookSalePackagingPrice;

    @Column(name = "booksale_packaging_type")
    private String bookSalePackagingType;

    @Column(name = "booksale_payment_amount")
    private Integer bookSalePaymentAmount;

    @Column(name = "booksale_state")
    private BookSaleState bookSaleState;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Pk implements Serializable {

        @Column(name = "sale_id")
        private Long saleId;

        @Column(name = "book_id")
        private Long bookId;
    }


}
