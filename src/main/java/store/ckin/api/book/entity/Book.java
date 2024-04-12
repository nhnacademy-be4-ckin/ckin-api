package store.ckin.api.book.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.file.entity.File;

/**
 * Book Entity.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "Book")
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "book_isbn")
    private String bookIsbn;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_description")
    private String bookDescription;

    @Column(name = "book_publisher")
    private String bookPublisher;

    @Column(name = "book_publication_date")
    private LocalDate bookPublicationDate;

    @Column(name = "book_index")
    private String bookIndex;

    @Column(name = "book_packaging")
    private Boolean bookPackaging;

    @Column(name = "book_state")
    @Builder.Default
    private String bookState = "ON_SALE";

    @Column(name = "book_stock")
    @Builder.Default
    private Integer bookStock = 0;

    @Column(name = "book_regular_price")
    private Integer bookRegularPrice;

    @Column(name = "book_discount_rate")
    private Integer bookDiscountRate;

    @Column(name = "book_sale_price")
    private Integer bookSalePrice;

    @Column(name = "book_review_rate")
    @Builder.Default
    private String bookReviewRate = "0";

    @LastModifiedDate
    @Column(name = "modification_time")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY)
    private File thumbnail;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookAuthor> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCategory> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> tags;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<BookSale> sales;

    public void updateBookReviewRate(Integer reviewRate) {
        this.bookReviewRate = String.valueOf(Integer.parseInt(this.bookReviewRate) + reviewRate);
    }

    public void updateBookStock(Integer stock) {
        this.bookStock -= stock;
    }
}
