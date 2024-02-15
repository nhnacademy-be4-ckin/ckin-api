package store.ckin.api.category.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.category.dto.request.CategoryCreateDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.entity.Category;
import store.ckin.api.category.repository.CategoryRepository;
import store.ckin.api.category.service.CategoryService;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto) {
        Category parentCategory = null;
        if (categoryCreateDto.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(categoryCreateDto.getParentCategoryId()).orElse(null);
        }

        Category category = Category.builder()
                .parentCategory(parentCategory)
                .categoryName(categoryCreateDto.getCategoryName())
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


}


