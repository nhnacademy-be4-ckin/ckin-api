package store.ckin.api.category.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import store.ckin.api.category.dto.request.CategoryCreateDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.service.CategoryService;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping("/")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryCreateDto categoryCreateDto) {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryCreateDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryResponseDto.getCategoryId())
                .toUri();

        return ResponseEntity.created(location).body(categoryResponseDto);
    }

    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryResponseDto>> getSubcategories(@PathVariable Long parentId) {
        List<CategoryResponseDto> subcategories = categoryService.findSubcategories(parentId);
        return ResponseEntity.ok(subcategories);
    }
}
