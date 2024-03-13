package store.ckin.api.book.relationship.booktag.entity;

import lombok.*;
import store.ckin.api.book.entity.Book;
import store.ckin.api.tag.entity.Tag;

import javax.persistence.*;
import java.io.Serializable;

/**
 * BookTag 연결 테이블.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "Book_Tag")
public class BookTag {

    @EmbeddedId
    private PK pk;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Embeddable
    @EqualsAndHashCode
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PK implements Serializable {
        private Long bookId;
        private Long tagId;
    }

}
