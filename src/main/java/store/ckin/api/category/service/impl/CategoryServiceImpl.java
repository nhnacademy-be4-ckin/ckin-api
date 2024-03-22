package store.ckin.api.category.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryNameResponseDto;
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
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private static final int DEFAULT_CATEGORY_PRIORITY = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto) {
        Category parentCategory = null;
        int categoryPriority = DEFAULT_CATEGORY_PRIORITY;

        if (categoryCreateRequestDto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findByCategoryId(categoryCreateRequestDto.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(categoryCreateRequestDto.getParentCategoryId()));

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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CategoryResponseDto> findSubcategories(Long parentId) {
        List<Category> subcategories = categoryRepository.findByParentCategory_CategoryId(parentId);
        return subcategories.stream()
                .map(category -> new CategoryResponseDto(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CategoryResponseDto> findTopCategories() {
        List<Category> topCategories = categoryRepository.findByParentCategoryIsNull();
        return topCategories.stream()
                .map(category -> new CategoryResponseDto(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryUpdateRequestDto categoryUpdateDto) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Category updatedCategory = existingCategory.toBuilder()
                .categoryName(categoryUpdateDto.getCategoryName())
                .build();
        updatedCategory = categoryRepository.save(updatedCategory);

        return new CategoryResponseDto(updatedCategory.getCategoryId(), updatedCategory.getCategoryName());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCategoryName(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return category.getCategoryName();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getParentIds(List<Long> bookIds) {
        return categoryRepository.getParentIds(bookIds);
    }


}


