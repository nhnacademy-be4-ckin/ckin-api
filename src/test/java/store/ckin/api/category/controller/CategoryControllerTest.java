package store.ckin.api.category.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.category.dto.request.CategoryCreateRequestDto;
import store.ckin.api.category.dto.request.CategoryUpdateRequestDto;
import store.ckin.api.category.dto.response.CategoryCacheResponseDto;
import store.ckin.api.category.dto.response.CategoryResponseDto;
import store.ckin.api.category.service.CategoryService;

/**
 * CategoryControllerTest.
 *
 * @author 나국로
 * @version 2024. 02. 21.
 */
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
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
                .andExpect(status().isCreated())
                .andDo(document("category/create/success",
                        requestFields(
                                fieldWithPath("parentCategoryId").description("부모 카테고리 ID"),
                                fieldWithPath("categoryName").description("카테고리 이름")
                        )
                ));

    }


    @Test
    @DisplayName("최상위 카테고리 조회 요청 시 상태 코드 200 반환")
    void whenGetTopCategories_thenStatusOk() throws Exception {
        List<CategoryResponseDto> topCategories = Arrays.asList(
                new CategoryResponseDto(1L, "카테고리1"),
                new CategoryResponseDto(2L, "카테고리2")
        );
        given(categoryService.findTopCategories()).willReturn(topCategories);

        mockMvc.perform(get("/api/categories/top")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("category/getTopCategories/success",
                        responseFields(
                                fieldWithPath("[].categoryId").description("카테고리 ID"),
                                fieldWithPath("[].categoryName").description("카테고리 이름")
                        )
                ));

    }

    @Test
    @DisplayName("부모 ID로 하위 카테고리 조회 요청 시 상태 코드 200 반환")
    void whenGetSubcategories_thenStatusOk() throws Exception {
        Long parentId = 1L;
        List<CategoryResponseDto> subcategories = Arrays.asList(
                new CategoryResponseDto(1L, "하위 카테고리 1"),
                new CategoryResponseDto(2L, "하위 카테고리 2")
        );
        given(categoryService.findSubcategories(parentId)).willReturn(subcategories);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{parentId}/subcategories", parentId))
                .andExpect(status().isOk())
                .andDo(document("category/getSubCategories/success",
                        pathParameters(
                                parameterWithName("parentId").description("상위 카테고리의 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].categoryId").description("하위 카테고리의 ID"),
                                fieldWithPath("[].categoryName").description("하위 카테고리의 이름")
                        )
                ));
    }


    @Test
    @DisplayName("카테고리 업데이트 요청 시 상태 코드 200 반환")
    void whenUpdateCategory_thenStatusOk() throws Exception {
        Long categoryId = 1L;
        CategoryUpdateRequestDto requestDto = new CategoryUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "categoryName", "외국도서");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "외국도서");
        when(categoryService.updateCategory(categoryId, requestDto)).thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(document("category/updateCategory/success",
                        requestFields(
                                fieldWithPath("categoryName").description("업데이트할 카테고리의 새 이름")
                        )
                ));
    }


    @Test
    @DisplayName("카테고리 삭제 요청 시 상태 코드 200 반환")
    void whenDeleteCategory_thenStatusOk() throws Exception {
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andDo(document("category/deleteCategory/success",
                        pathParameters(
                                parameterWithName("categoryId").description("삭제할 카테고리의 ID")
                        )
                ));
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
                .andExpect(status().isBadRequest())
                .andDo(document("category/createCategory/validation-failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
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
                .andExpect(status().isBadRequest())
                .andDo(document("category/updateCategory/validation-failed",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
    @Test
    @DisplayName("모든 카테고리 조회 요청 시 상태 코드 200 반환")
    void whenGetAllCategories_thenStatusOk() throws Exception {
        // Given
        List<CategoryCacheResponseDto> categories = Arrays.asList(
                new CategoryCacheResponseDto(1L, null, "국내도서", 1),
                new CategoryCacheResponseDto(2L, 1L, "외국도서", 2)
        );
        given(categoryService.getAllCategories()).willReturn(categories);

        // When and Then
        mockMvc.perform(get("/api/categories/redis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()))
                .andDo(document("category/getAllCategories/success",
                        responseFields(
                                fieldWithPath("[0].categoryId").description("카테고리 ID"),
                                fieldWithPath("[0].parentCategoryId").description("부모 카테고리 ID").optional(),
                                fieldWithPath("[0].categoryName").description("카테고리 이름"),
                                fieldWithPath("[0].categoryPriority").description("카테고리 우선순위")
                        )
                ));
    }


    @Test
    @DisplayName("주어진 카테고리 ID에 대한 이름 조회 요청 시 상태 코드 200 반환")
    void whenGetCategoryName_thenStatusOk() throws Exception {
        Long categoryId = 1L;
        String expectedCategoryName = "국내도서";
        given(categoryService.getCategoryName(categoryId)).willReturn(expectedCategoryName);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/get/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCategoryName))
                .andDo(document("category/getCategoryNames/success",
                        pathParameters(
                                parameterWithName("categoryId").description("조회할 카테고리의 ID")
                        )
                ));
    }
    @Test
    @DisplayName("부모 카테고리 ID 조회 요청 시 상태 코드 200 반환")
    void whenGetParentIds_thenStatusOk() throws Exception {
        List<Long> bookIds = Arrays.asList(1L, 2L, 3L);
        List<Long> parentIds = Arrays.asList(10L, 20L);

        given(categoryService.getParentIds(bookIds)).willReturn(parentIds);

        mockMvc.perform(get("/api/categories/parentIds")
                        .param("bookIds", "1", "2", "3"))
                .andExpect(status().isOk())
                .andDo(document("category/getParentIds",
                        requestParameters(
                                parameterWithName("bookIds").description("책 ID 목록")
                        ),
                        responseFields(
                                fieldWithPath("[]").description("부모 카테고리 ID 목록")
                        )
                ));

    }

}
