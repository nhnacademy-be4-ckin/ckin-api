package store.ckin.api.category.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.category.entity.Category;

/**
 * CategoryRepository.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    /**
     * 주어진 카테고리 ID에 해당하는 카테고리를 조회합니다.
     *
     * @param parentCategoryId 부모 카테고리 ID
     * @return 해당 카테고리가 존재할 경우 그 카테고리에 대한 Optional 객체
     */
    Optional<Category> findByCategoryId(Long parentCategoryId);


    /**
     * 부모 카테고리가 없는 최상위 카테고리 목록을 조회합니다.
     *
     * @return 최상위 카테고리 목록
     */
    List<Category> findByParentCategoryIsNull();

    /**
     * 주어진 부모 카테고리 ID에 해당하는 하위 카테고리 목록을 조회합니다.
     *
     * @param parentId 부모 카테고리 ID
     * @return 해당 부모 카테고리의 하위 카테고리 목록
     */
    List<Category> findByParentCategory_CategoryId(Long parentId);
}

