package store.ckin.api.booksale.entity;

import lombok.*;
import store.ckin.api.book.entity.Book;
import store.ckin.api.sale.entity.Sale;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 주문 도서 (리스트) Entity.
 *
 * @author 정승조
 * @version 2024. 02. 27.
 */

@ToString
@Getter
@Entity
@Table(name = "BookSale")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookSale {


    public enum BookSaleState {
        ORDER, RETURN, CANCEL, COMPLETE
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
    @ToString
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

    @Builder
    public BookSale(Pk pk, Sale sale, Book book, Long couponId, Integer bookSaleQuantity,
                    Integer bookSalePackagingPrice,
                    String bookSalePackagingType, Integer bookSalePaymentAmount, BookSaleState bookSaleState) {
        this.pk = pk;
        this.sale = sale;
        this.book = book;
        this.couponId = couponId;
        this.bookSaleQuantity = bookSaleQuantity;
        this.bookSalePackagingPrice = bookSalePackagingPrice;
        this.bookSalePackagingType = bookSalePackagingType;
        this.bookSalePaymentAmount = bookSalePaymentAmount;
        this.bookSaleState = bookSaleState;
    }


    public void updateBookSaleState(BookSaleState state) {
        this.bookSaleState = state;
    }

}
