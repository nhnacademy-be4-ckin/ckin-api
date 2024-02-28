package store.ckin.api.book.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;

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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookAuthor> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCategory> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> tags;

}
