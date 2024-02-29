package store.ckin.api.category.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.service.CategoryService;

/**
 * CategoryControllerTest.
 *
 * @author 나국로
 * @version 2024. 02. 21.
 */
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("카테고리 생성 요청 시 성공 및 상태 코드 201 반환")
    void whenCreateCategory_thenStatusCreated() throws Exception {
        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "parentCategoryId", 1L);
        ReflectionTestUtils.setField(requestDto, "categoryName", "카테고리 테스트");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        when(categoryService.createCategory(requestDto)).thenReturn(new CategoryResponseDto());

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("최상위 카테고리 조회 요청 시 상태 코드 200 반환")
    void whenGetTopCategories_thenStatusOk() throws Exception {
        given(categoryService.findTopCategories()).willReturn(List.of());

        mockMvc.perform(get("/api/categories/top"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetSubcategories_thenStatusOk() throws Exception {
        Long parentId = 1L;
        given(categoryService.findSubcategories(parentId)).willReturn(List.of());

        mockMvc.perform(get("/api/categories/" + parentId + "/subcategories"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("부모 ID로 하위 카테고리 조회 요청 시 상태 코드 200 반환")
    void whenUpdateCategory_thenStatusOk() throws Exception {
        Long categoryId = 1L;
        CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "categoryName", "외국도서");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "외국도서");
        when(categoryService.updateCategory(categoryId, requestDto)).thenReturn(responseDto);

        mockMvc.perform(put("/api/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("카테고리 업데이트 요청 시 상태 코드 200 반환")
    void whenDeleteCategory_thenStatusOk() throws Exception {
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/categories/" + categoryId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 입력값으로 카테고리 생성 요청 시 상태 코드 400 반환 검증")
    void givenInvalidCreateRequest_whenCreateCategory_thenStatusBadRequest() throws Exception {
        CategoryCreateRequestDto invalidRequest = new CategoryCreateRequestDto();
        ReflectionTestUtils.setField(invalidRequest, "categoryName", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 입력값으로 카테고리 업데이트 요청 시 상태 코드 400 반환 검증")
    void givenInvalidUpdateRequest_whenUpdateCategory_thenStatusBadRequest() throws Exception {
        long categoryId = 1L;
        CategoryUpdateRequestDto invalidRequest = new CategoryUpdateRequestDto();
        ReflectionTestUtils.setField(invalidRequest, "categoryName", "a".repeat(11));
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(put("/api/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }


}
