package store.ckin.api.book.relationship.booktag.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.book.entity.Book;
import store.ckin.api.tag.entity.Tag;

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
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @MapsId("tagId")
    @ManyToOne
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
