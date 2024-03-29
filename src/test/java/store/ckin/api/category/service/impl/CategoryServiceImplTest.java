package store.ckin.api.category.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryCacheResponseDto;
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
        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "categoryName", "국내도서");
        Category category = Category.builder()
                .categoryId(null)
                .parentCategory(null)
                .categoryName("국내도서")
                .categoryPriority(1)
                .build();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto);

        assertThat(createdCategory.getCategoryName()).isEqualTo("국내도서");
    }

    @Test
    @DisplayName("하위 카테고리 조회 테스트")
    void whenFindSubcategories_thenSubcategoriesAreReturned() {
        Long parentId = 1L;
        List<Category> subcategories = List.of(
                Category.builder().categoryName("소설").categoryPriority(2).build(),
                Category.builder().categoryName("시").categoryPriority(2).build()
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
                Category.builder().categoryName("국내도서").categoryPriority(1).build(),
                Category.builder().categoryName("외국도서").categoryPriority(1).build()
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
        Category originalCategory = Category.builder()
                .categoryId(categoryId)
                .categoryName("국내도서")
                .categoryPriority(1)
                .build();
        CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "categoryName", "외국도서");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(originalCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(
                Category.builder()
                        .categoryId(categoryId)
                        .categoryName("외국도서")
                        .categoryPriority(1)
                        .build());

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
        CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "categoryName", "외국도서");

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

    @Test
    @DisplayName("부모 카테고리가 존재하지 않을 때 CategoryNotFoundException 발생")
    void whenParentCategoryNotFound_thenThrowCategoryNotFoundException() {
        Long nonExistingParentId = 999L;
        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto();

        ReflectionTestUtils.setField(requestDto, "parentCategoryId", nonExistingParentId);
        ReflectionTestUtils.setField(requestDto, "categoryName", "테스트 카테고리");

        when(categoryRepository.findByCategoryId(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.createCategory(requestDto);
        });
    }

    @Test
    @DisplayName("유효한 카테고리 ID로 카테고리 이름 조회")
    void givenCategoryId_whenGetCategoryName_thenReturnsName() {
        Long categoryId = 1L;
        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("국내도서")
                .categoryPriority(1)
                .build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        String categoryName = categoryService.getCategoryName(categoryId);

        assertThat(categoryName).isEqualTo(category.getCategoryName());
    }


    @Test
    @DisplayName("유효하지 않은 카테고리 ID로 이름 조회 시 예외 발생")
    void givenInvalidCategoryId_whenGetCategoryName_thenThrowsException() {
        Long invalidCategoryId = 2L;
        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryName(invalidCategoryId))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("책 ID 목록으로 부모 카테고리 ID 목록 조회")
    void whenGetParentIds_givenBookIds_thenReturnsCategoryIds() {
        List<Long> bookIds = Arrays.asList(1L, 2L, 3L);
        List<Long> expectedCategoryIds = Arrays.asList(10L, 20L);
        when(categoryRepository.getParentIds(bookIds)).thenReturn(expectedCategoryIds);

        List<Long> actualCategoryIds = categoryService.getParentIds(bookIds);

        assertThat(actualCategoryIds).isEqualTo(expectedCategoryIds);
    }

    @Test
    void whenGetAllCategories_thenReturnsCategoryCacheResponseDtos() {
        CategoryCacheResponseDto parentCategoryDto = CategoryCacheResponseDto.builder()
                .categoryId(1L)
                .categoryName("부모 카테고리")
                .categoryPriority(1)
                .build();

        CategoryCacheResponseDto childCategoryDto = CategoryCacheResponseDto.builder()
                .categoryId(2L)
                .parentCategoryId(1L)
                .categoryName("자식 카테고리")
                .categoryPriority(2)
                .build();

        Category parentCategory = Category.builder()
                .categoryId(1L)
                .categoryName("부모 카테고리")
                .categoryPriority(1)
                .build();
        Category childCategory = Category.builder()
                .categoryId(2L)
                .parentCategory(parentCategory)
                .categoryName("자식 카테고리")
                .categoryPriority(2)
                .build();
        List<Category> categories = Arrays.asList(parentCategory, childCategory);


        when(categoryRepository.findAll()).thenReturn(categories);


        List<CategoryCacheResponseDto> expectedCategories = Arrays.asList(parentCategoryDto, childCategoryDto);


        List<CategoryCacheResponseDto> actualCategories = categoryService.getAllCategories();

        //컨텐츠 검증
        assertThat(actualCategories).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedCategories);
    }

}
