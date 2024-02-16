package store.ckin.api.category.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.entity.Category;
import store.ckin.api.category.exception.CategoryNotFoundException;
import store.ckin.api.category.repository.CategoryRepository;
import store.ckin.api.category.service.CategoryService;

/**
 * CategoryServiceImpl.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto) {
        Category parentCategory = null;
        int categoryPriority = 1; // 기본 우선순위

        if (categoryCreateRequestDto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findByCategoryId(categoryCreateRequestDto.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("부모 카테고리가 존재하지 않습니다."));

            // 부모 카테고리의 우선순위에 1을 더함
            categoryPriority = parentCategory.getCategoryPriority() + 1;
        }

        Category category = Category.builder()
                .parentCategory(parentCategory)
                .categoryName(categoryCreateRequestDto.getCategoryName())
                .categoryPriority(categoryPriority)
                .build();
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponseDto.builder()
                .categoryId(savedCategory.getCategoryId())
                .categoryName(savedCategory.getCategoryName())
                .build();
    }

    @Override
    public List<CategoryResponseDto> findSubcategories(Long parentId) {
        List<Category> subcategories = categoryRepository.findByParentCategory_CategoryId(parentId);
        return subcategories.stream()
                .map(category -> new CategoryResponseDto(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDto> findTopCategories() {
        List<Category> topCategories = categoryRepository.findByParentCategoryIsNull();
        return topCategories.stream()
                .map(category -> new CategoryResponseDto(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryUpdateRequestDto categoryUpdateDto) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리가 존재하지 않습니다."));

        // 카테고리 이름 업데이트
        Category updatedCategory = existingCategory.toBuilder()
                .categoryName(categoryUpdateDto.getCategoryName())
                .build();
        updatedCategory = categoryRepository.save(updatedCategory); // 업데이트된 인스턴스 저장

        return new CategoryResponseDto(updatedCategory.getCategoryId(), updatedCategory.getCategoryName());
    }



    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }



}


