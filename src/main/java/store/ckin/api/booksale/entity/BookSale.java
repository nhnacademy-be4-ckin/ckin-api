package store.ckin.api.booksale.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.book.entity.Book;
import store.ckin.api.sale.entity.Sale;

/**
 * 주문 도서 (리스트) Entity.
 *
 * @author 정승조
 * @version 2024. 02. 27.
 */

@Getter
@Entity
@Table(name = "BookSale")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookSale {


    public enum BookSaleState {
        ORDER, CANCEL
    }

    @EmbeddedId
    private Pk pk;

    @MapsId("saleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

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
    @Enumerated(EnumType.STRING)
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

    public void updateBookSaleState(BookSaleState state) {
        this.bookSaleState = state;
    }

}
