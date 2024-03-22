package store.ckin.api.category.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.service.CategoryService;

/**
 * CategoryController 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    /**
     * 새로운 카테고리를 생성합니다.
     *
     * @param categoryCreateRequestDto 카테고리 생성 요청 DTO
     * @return 생성 성공시 상태 코드 201과 함께 ResponseEntity 반환
     */
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto) {
        categoryService.createCategory(categoryCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 최상위 카테고리 목록을 조회합니다.
     *
     * @return 최상위 카테고리 목록이 담긴 ResponseEntity 객체
     */
    @GetMapping("/top")
    public ResponseEntity<List<CategoryResponseDto>> getTopCategories() {
        List<CategoryResponseDto> topCategories = categoryService.findTopCategories();
        return ResponseEntity.ok(topCategories);
    }


    /**
     * 지정된 부모 ID를 가진 하위 카테고리 목록을 조회합니다.
     *
     * @param parentId 부모 카테고리 ID
     * @return 하위 카테고리 목록이 담긴 ResponseEntity 객체
     */
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryResponseDto>> getSubcategories(@PathVariable Long parentId) {
        List<CategoryResponseDto> subcategories = categoryService.findSubcategories(parentId);
        return ResponseEntity.ok(subcategories);
    }

    /**
     * 지정된 ID의 카테고리를 업데이트합니다.
     *
     * @param categoryId               카테고리 ID
     * @param categoryUpdateRequestDto 카테고리 업데이트 요청 DTO
     * @return 업데이트 성공시 상태 코드 200과 함께 ResponseEntity 반환
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId,
                                               @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto) {
        categoryService.updateCategory(categoryId, categoryUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 지정된 ID의 카테고리를 삭제합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 삭제 성공시 상태 코드 200과 함께 ResponseEntity 반환
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 이름을 가져옵니다.
     *
     * @param categoryId
     * @return 카테고리 이름
     */
    @GetMapping("/get/{categoryId}")
    public ResponseEntity<String> getCategoryName(@PathVariable("categoryId") Long categoryId) {
        String categoryName = categoryService.getCategoryName(categoryId);

        return ResponseEntity.ok().body(categoryName);
    }

    /**
     * 카테고리 아이디에 해당하는 부모 카테고리 아이디를 포함하여 반환합니다.
     *
     * @param bookIds 도서 아이디 목록
     * @return 부모 카테고리 아이디 목록
     */
    @GetMapping("/parentIds")
    public ResponseEntity<List<Long>> getParentIds(@RequestParam List<Long> bookIds) {
        List<Long> content = categoryService.getParentIds(bookIds);

        return ResponseEntity.ok().body(content);
    }
}
