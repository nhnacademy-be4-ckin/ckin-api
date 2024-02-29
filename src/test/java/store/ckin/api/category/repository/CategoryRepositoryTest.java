package store.ckin.api.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.category.entity.Category;


/**
 * CategoryRepositoryTest.
 *
 * @author 나국로
 * @version 2024. 02. 21.
 */
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("새 카테고리 생성 및 조회")
    void givenNewCategory_whenSave_thenFindById() {

        Category newCategory = Category.builder()
                .categoryName("국내도서")
                .categoryPriority(1)
                .build();

        categoryRepository.save(newCategory);

        Optional<Category> foundCategory = categoryRepository.findById(newCategory.getCategoryId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCategoryName()).isEqualTo("국내도서");
        assertThat(foundCategory.get().getCategoryPriority()).isEqualTo(1);
    }

    @Test
    @DisplayName("카테고리 정보 업데이트 및 검증")
    void givenExistingCategory_whenUpdate_thenFindUpdatedCategory() {
        Category originalCategory = Category.builder()
                .categoryName("국내소설")
                .categoryPriority(2)
                .build();
        categoryRepository.save(originalCategory);

        Category updatedCategory = originalCategory.toBuilder()
                .categoryName("외국소설")
                .build();
        categoryRepository.save(updatedCategory);

        Optional<Category> foundCategory = categoryRepository.findById(updatedCategory.getCategoryId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCategoryName()).isEqualTo("외국소설");
    }

    @Test
    @DisplayName("특정 ID로 카테고리 조회")
    void givenCategoryId_whenFindByCategoryId_thenCategoryIsReturned() {
        Category category = Category.builder()
                .categoryName("국내도서")
                .build();
        categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findByCategoryId(category.getCategoryId());

        assertThat(found).isPresent();
        assertThat(found.get().getCategoryName()).isEqualTo("국내도서");
    }

    @Test
    @DisplayName("최상위 카테고리 조회")
    void whenFindRootCategories_thenRootCategoriesAreReturned() {
        Category rootCategory = Category.builder()
                .categoryName("최상위 카테고리")
                .build();
        categoryRepository.save(rootCategory);

        List<Category> rootCategories = categoryRepository.findByParentCategoryIsNull();

        assertThat(rootCategories).isNotEmpty();
        assertThat(rootCategories.get(0).getCategoryName()).isEqualTo("최상위 카테고리");
    }

    @Test
    @DisplayName("부모 ID로 하위 카테고리 조회")
    void givenParentCategoryId_whenFindChildCategories_thenChildCategoriesAreReturned() {

        Category parentCategory = Category.builder()
                .categoryName("부모 카테고리")
                .build();
        categoryRepository.save(parentCategory);

        Category childCategory = Category.builder()
                .parentCategory(parentCategory)
                .categoryName("자식 카테고리")
                .build();
        categoryRepository.save(childCategory);

        List<Category> childCategories =
                categoryRepository.findByParentCategory_CategoryId(parentCategory.getCategoryId());

        assertThat(childCategories).isNotEmpty();
        assertThat(childCategories.get(0).getCategoryName()).isEqualTo("자식 카테고리");
    }

}
