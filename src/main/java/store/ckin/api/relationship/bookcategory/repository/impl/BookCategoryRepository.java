package store.ckin.api.relationship.bookcategory.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.relationship.bookcategory.entity.BookCategory;

/**
 * 상품카테고리 관계 테이블레포지토리.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategory.PK> {
}
