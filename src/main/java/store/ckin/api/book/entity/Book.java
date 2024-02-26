package store.ckin.api.book.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.relationship.booktag.entity.BookTag;

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
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "book_isbn", nullable = false, unique = true, length = 13)
    private String bookIsbn;

    @Column(name = "book_title", nullable = false, length = 100)
    private String bookTitle;

    @Column(name = "book_description", nullable = false, columnDefinition = "TEXT")
    private String bookDescription;

    @Column(name = "book_publisher", nullable = false, length = 100)
    private String bookPublisher;

    @Column(name = "book_publication_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date bookPublicationDate;

    @Column(name = "book_index", columnDefinition = "TEXT")
    private String bookIndex;

    @Column(name = "book_packaging", nullable = false)
    private Boolean bookPackaging;

    @Column(name = "book_state", nullable = false, length = 20)
    private String bookState = "ON_SALE";

    @Column(name = "book_stock", nullable = false)
    @Builder.Default
    private Integer bookStock = 0;

    @Column(name = "book_regular_price", nullable = false)
    private Integer bookRegularPrice;

    @Column(name = "book_discount_rate", nullable = false)
    private Integer bookDiscountRate;

    @Column(name = "book_sale_price", nullable = false)
    private Integer bookSalePrice;

    @Column(name = "book_review_rate", nullable = false, length = 5)
    @Builder.Default
    private String bookReviewRate = "0";

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookAuthor> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCategory> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> tags;

}
