package store.ckin.api.book.relationship.bookauthor.entity;

import lombok.*;
import store.ckin.api.author.entity.Author;
import store.ckin.api.book.entity.Book;

import javax.persistence.*;
import java.io.Serializable;

/**
 * BookAuthor 연결 테이블.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "Book_Author")
public class BookAuthor {

    @EmbeddedId
    private PK pk;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @MapsId("authorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @EqualsAndHashCode
    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PK implements Serializable {
        private Long bookId;
        private Long authorId;
    }
}
