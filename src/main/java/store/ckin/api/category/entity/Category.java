package store.ckin.api.category.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;

/**
 * Category 엔티티 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Entity
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private Category parentCategory;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_priority")
    private Integer categoryPriority;


    @OneToMany(mappedBy = "category")
    private Set<BookCategory> books;
}

