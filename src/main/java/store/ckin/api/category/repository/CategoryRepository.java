package store.ckin.api.category.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.ckin.api.category.entity.Category;

/**
 * CategoryRepository.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryId(Long parentCategoryId);


    //최상위 카테고리 조회
    List<Category> findByParentCategoryIsNull();

    // 특정 카테고리의 하위 카테고리들 조회
    List<Category> findByParentCategory_CategoryId(Long parentId);
}

