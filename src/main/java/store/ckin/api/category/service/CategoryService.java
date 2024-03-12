package store.ckin.api.category.service;

import java.util.List;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;

/**
 * CategoryService.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
public interface CategoryService {

    /**
     * 새로운 카테고리를 생성하고 결과를 반환합니다.
     *
     * @param categoryCreateRequestDto 카테고리 생성 요청 DTO
     * @return 생성된 카테고리에 대한 응답 DTO
     */
    CategoryResponseDto createCategory(CategoryCreateRequestDto categoryCreateRequestDto);

    /**
     * 최상위 카테고리 목록을 조회합니다.
     *
     * @return 최상위 카테고리에 대한 응답 DTO 리스트
     */
    List<CategoryResponseDto> findTopCategories();


    /**
     * 지정된 부모 ID를 가진 하위 카테고리 목록을 조회합니다.
     *
     * @param parentId 부모 카테고리 ID
     * @return 하위 카테고리에 대한 응답 DTO 리스트
     */
    List<CategoryResponseDto> findSubcategories(Long parentId);

    /**
     * 지정된 ID를 가진 카테고리를 업데이트하고 결과를 반환합니다.
     *
     * @param categoryId        카테고리 ID
     * @param categoryUpdateDto 카테고리 업데이트 요청 DTO
     * @return 업데이트된 카테고리에 대한 응답 DTO
     */
    CategoryResponseDto updateCategory(Long categoryId, CategoryUpdateRequestDto categoryUpdateDto);


    /**
     * 지정된 ID를 가진 카테고리를 삭제합니다.
     *
     * @param categoryId 카테고리 ID
     */
    void deleteCategory(Long categoryId);

}
