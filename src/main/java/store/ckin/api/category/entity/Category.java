package store.ckin.api.category.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Entity
@Getter
@NoArgsConstructor
@Table
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private Category parentCategory;

    @Column(name = "category_name")
    private String categoryName;

    //없어도 될 거 같음
//    @Column(name = "category_priority")
//    private Integer categoryPriority;


    @Builder
    public Category(Long categoryId, Category parentCategory, String categoryName) {
        this.categoryId = categoryId;
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
    }
}

