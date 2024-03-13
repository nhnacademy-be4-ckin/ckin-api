package store.ckin.api.book.relationship.bookcategory.entity;

import lombok.*;
import store.ckin.api.book.entity.Book;
import store.ckin.api.category.entity.Category;

import javax.persistence.*;
import java.io.Serializable;

/**
 * BookCategory 연결 테이블.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "Book_Category")
public class BookCategory {

    @EmbeddedId
    private PK pk;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @EqualsAndHashCode
    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PK implements Serializable {
        private Long bookId;
        private Long categoryId;
    }

}
