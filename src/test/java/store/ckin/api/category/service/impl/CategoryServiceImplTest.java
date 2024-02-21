package store.ckin.api.category.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.entity.Category;
import store.ckin.api.category.exception.CategoryNotFoundException;
import store.ckin.api.category.repository.CategoryRepository;

/**
 * CategoryServiceImplTest.
 *
 * @author 나국로
 * @version 2024. 02. 21.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("카테고리 생성 테스트")
    void whenCreateCategory_thenCategoryIsCreated() {
        String categoryName = "국내도서";
        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto(null, categoryName, null);
        Category category = new Category(null, null, "국내도서", 1);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto);

        assertThat(createdCategory.getCategoryName()).isEqualTo("국내도서");
    }

    @Test
    @DisplayName("하위 카테고리 조회 테스트")
    void whenFindSubcategories_thenSubcategoriesAreReturned() {
        Long parentId = 1L;
        List<Category> subcategories = List.of(
                new Category(null, new Category(), "소설", 2),
                new Category(null, new Category(), "시", 2)
        );
        when(categoryRepository.findByParentCategory_CategoryId(parentId)).thenReturn(subcategories);

        List<CategoryResponseDto> result = categoryService.findSubcategories(parentId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("소설");
        assertThat(result.get(1).getCategoryName()).isEqualTo("시");
    }

    @Test
    @DisplayName("최상위 카테고리 조회 테스트")
    void whenFindTopCategories_thenTopCategoriesAreReturned() {
        List<Category> topCategories = List.of(
                new Category(null, null, "국내도서", 1),
                new Category(null, null, "외국도서", 1)
        );
        when(categoryRepository.findByParentCategoryIsNull()).thenReturn(topCategories);

        List<CategoryResponseDto> result = categoryService.findTopCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("국내도서");
        assertThat(result.get(1).getCategoryName()).isEqualTo("외국도서");
    }

    @Test
    @DisplayName("카테고리 업데이트 테스트")
    void whenUpdateCategory_thenCategoryIsUpdated() {
        Long categoryId = 1L;
        Category originalCategory = new Category(categoryId, null, "국내도서", 1);
        CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto("외국도서");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(originalCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category(categoryId, null, "외국도서", 1));

        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, requestDto);

        assertThat(updatedCategory.getCategoryName()).isEqualTo("외국도서");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void whenDeleteCategory_thenCategoryIsDeleted() {
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 업데이트 시 예외 발생")
    void givenNonExistingCategoryId_whenUpdateCategory_thenThrowCategoryNotFoundException() {

        Long nonExistingCategoryId = 1L;
        CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto("외국도서");

        when(categoryRepository.findById(nonExistingCategoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.updateCategory(nonExistingCategoryId, requestDto)
        );
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 삭제 시 예외 발생")
    void givenNonExistingCategoryId_whenDeleteCategory_thenThrowCategoryNotFoundException() {
        Long nonExistingCategoryId = 1L;
        when(categoryRepository.existsById(nonExistingCategoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.deleteCategory(nonExistingCategoryId)
        );
    }
}
