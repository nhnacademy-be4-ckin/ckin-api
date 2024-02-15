package store.ckin.api.author.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "author_name")
    private String authorName;

    @Builder
    public Author(Long authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }
}
