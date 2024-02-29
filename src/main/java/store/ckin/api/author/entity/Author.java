package store.ckin.api.author.entity;

import java.util.Set;
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

/**
 * Author 테이블.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Author")
@AllArgsConstructor
@Builder(toBuilder = true)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "author_name")
    private String authorName;

    @OneToMany(mappedBy = "author")
    private Set<BookAuthor> books;


}
