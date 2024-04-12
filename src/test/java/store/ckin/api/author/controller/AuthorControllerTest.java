package store.ckin.api.author.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.service.AuthorService;

/**
 * AuthorControllerTest.
 *
 * @author 나국로
 * @version 2024. 02. 19.
 */
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("ID로 작가 조회")
    void givenAuthorId_whenGetAuthorById_thenReturnsAuthor() throws Exception {
        Long authorId = 1L;
        AuthorResponseDto authorResponseDto = new AuthorResponseDto(authorId, "김작가");
        when(authorService.findAuthorById(authorId)).thenReturn(authorResponseDto);

        mockMvc.perform(get("/api/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId", equalTo(authorId.intValue())))
                .andExpect(jsonPath("$.authorName", equalTo("김작가")))
                .andDo(print())
                .andDo(document("author/getAuthorById/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("authorId").description("작가의 ID."),
                                fieldWithPath("authorName").description("작가의 이름.")
                        )
                ));
    }


    @Test
    @DisplayName("이름으로 작가 검색")
    void givenAuthorName_whenSearchAuthorsByName_thenReturnsAuthors() throws Exception {
        String name = "작가";
        int page = 0;
        int size = 2;

        List<AuthorResponseDto> authorList = List.of(
                new AuthorResponseDto(1L, "작가1"),
                new AuthorResponseDto(2L, "작가2")
        );
        Page<AuthorResponseDto> mockResponse =
                new PageImpl<>(authorList, PageRequest.of(page, size), authorList.size());

        when(authorService.findAuthorsByName(eq(name), any(Pageable.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/authors/search")
                        .param("name", name)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(mockResponse.getContent().size())))
                .andExpect(jsonPath("$.content[0].authorId", equalTo(1)))
                .andExpect(jsonPath("$.content[0].authorName", equalTo("작가1")))
                .andExpect(jsonPath("$.content[1].authorId", equalTo(2)))
                .andExpect(jsonPath("$.content[1].authorName", equalTo("작가2")))
                .andExpect(jsonPath("$.totalPages", equalTo(mockResponse.getTotalPages())))
                .andExpect(jsonPath("$.totalElements", equalTo((int) mockResponse.getTotalElements())))
                .andExpect(jsonPath("$.size", equalTo(size)))
                .andExpect(jsonPath("$.number", equalTo(page)))
                .andDo(print())
                .andDo(document("author/searchAuthorsByName/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("name").description("검색할 작가의 이름"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("content").description("검색된 작가 목록"),
                                fieldWithPath("content[].authorId").description("작가의 ID"),
                                fieldWithPath("content[].authorName").description("작가의 이름"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 요소 수"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                subsectionWithPath("pageable").ignored(),
                                fieldWithPath("sort.empty").description("정렬된 데이터가 비어 있는지 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 데이터인지 여부"),
                                fieldWithPath("sort.sorted").description("정렬된 데이터인지 여부"),
                                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("first").description("현재 페이지가 첫 번째 페이지인지 여부"),
                                fieldWithPath("size").description("페이지당 요소의 수"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                                fieldWithPath("empty").description("현재 페이지가 비어 있는지 여부")
                        )
                ));
    }


    @Test
    @DisplayName("작가 생성 요청 시 작가 생성")
    void givenAuthorCreateRequest_whenCreateAuthor_thenAuthorCreated() throws Exception {
        AuthorCreateRequestDto createRequest = new AuthorCreateRequestDto();
        ReflectionTestUtils.setField(createRequest, "authorName", "김작가");
        AuthorResponseDto createdAuthor = new AuthorResponseDto(1L, "김작가");
        when(authorService.createAuthor(any(AuthorCreateRequestDto.class))).thenReturn(createdAuthor);

        String json = new ObjectMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorName", equalTo("김작가")))
                .andDo(print())
                .andDo(document("author/createAuthor/success",
                        requestFields(
                                fieldWithPath("authorName").description("생성할 작가의 이름")
                        ),
                        responseFields(
                                fieldWithPath("authorId").description("생성된 작가의 식별자"),
                                fieldWithPath("authorName").description("생성된 작가의 이름")
                        )
                ));

    }

    @Test
    @DisplayName("작가 정보 업데이트")
    void updateAuthorTest() throws Exception {
        Long authorId = 1L;
        AuthorModifyRequestDto modifyRequest = new AuthorModifyRequestDto();
        ReflectionTestUtils.setField(modifyRequest, "authorName", "김업뎃");
        AuthorResponseDto updatedAuthor = new AuthorResponseDto(authorId, "김업뎃");
        when(authorService.updateAuthor(eq(authorId), any(AuthorModifyRequestDto.class))).thenReturn(updatedAuthor);
        String json = new ObjectMapper().writeValueAsString(modifyRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", equalTo("김업뎃")))
                .andDo(print())
                .andDo(document("author/updateAuthor/success",
                        pathParameters(
                                parameterWithName("id").description("업데이트할 작가의 식별자")
                        ),
                        requestFields(
                                fieldWithPath("authorName").description("업데이트할 작가의 새로운 이름")
                        ),
                        responseFields(
                                fieldWithPath("authorId").description("업데이트된 작가의 식별자"),
                                fieldWithPath("authorName").description("업데이트된 작가의 이름")
                        )
                ));

    }

    @Test
    @DisplayName("작가 ID로 작가 삭제")
    void givenAuthorId_whenDeleteAuthor_thenAuthorDeleted() throws Exception {
        Long authorId = 1L;
        doNothing().when(authorService).deleteAuthor(authorId);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("author/delete/success",
                        pathParameters(
                                parameterWithName("id").description("삭제할 작가의 ID")
                        )
                ));

    }

    @Test
    @DisplayName("잘못된 입력 시 400 반환")
    void whenInvalidInput_thenReturns400() throws Exception {
        AuthorCreateRequestDto dto = new AuthorCreateRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "");
        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(document("author/createAuthor/validation-failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("수정폼 잘못된 입력 시 400 반환")
    void whenInvalidInputForUpdate_thenReturns400() throws Exception {
        Long authorId = 1L;
        AuthorModifyRequestDto dto = new AuthorModifyRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", "");

        mockMvc.perform(put("/api/authors/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(document("author/updateAuthor/validation-failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("생성 시 작가 이름이 너무 길면 400 반환")
    void whenAuthorNameTooLongInCreate_thenReturns400() throws Exception {
        String longName = "a".repeat(201);
        AuthorCreateRequestDto dto = new AuthorCreateRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", longName);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(document("author/createAuthor/validation-failed-too-Long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("업데이트 시 작가 이름이 너무 길면 400 반환")
    void whenAuthorNameTooLongInUpdate_thenReturns400() throws Exception {
        Long authorId = 1L;
        String longName = "a".repeat(201);
        AuthorModifyRequestDto dto = new AuthorModifyRequestDto();
        ReflectionTestUtils.setField(dto, "authorName", longName);

        mockMvc.perform(put("/api/authors/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(document("author/updateAuthor/validation-failed-too-Long",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }


    @Test
    @DisplayName("페이지 요청으로 모든 작가 조회")
    void givenPageRequest_whenGetAllAuthors_thenReturnsPagedAuthors() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<AuthorResponseDto> authorsList = Arrays.asList(
                new AuthorResponseDto(1L, "작가 1"),
                new AuthorResponseDto(2L, "작가 2")
        );
        Page<AuthorResponseDto> authorsPage = new PageImpl<>(authorsList, pageable, authorsList.size());

        when(authorService.findAllAuthors(pageable)).thenReturn(authorsPage);

        mockMvc.perform(get("/api/authors?page=" + page + "&size=" + size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(authorsList.size())))
                .andExpect(jsonPath("$.content[0].authorId", equalTo(1)))
                .andExpect(jsonPath("$.content[0].authorName", equalTo("작가 1")))
                .andExpect(jsonPath("$.content[1].authorId", equalTo(2)))
                .andExpect(jsonPath("$.content[1].authorName", equalTo("작가 2")))
                .andExpect(jsonPath("$.size", equalTo(size)))
                .andExpect(jsonPath("$.number", equalTo(page)))
                .andDo(print())
                .andDo(document("author/getAll/success",
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("content").description("조회된 작가들의 목록"),
                                fieldWithPath("content[].authorId").description("작가의 ID"),
                                fieldWithPath("content[].authorName").description("작가의 이름"),
                                subsectionWithPath("pageable").ignored(),
                                fieldWithPath("totalElements").description("전체 도서 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("first").description("현재 페이지가 첫 번째 페이지인지 여부"),
                                fieldWithPath("sort.empty").description("정렬된 데이터가 비어 있는지 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 데이터인지 여부"),
                                fieldWithPath("sort.sorted").description("정렬된 데이터인지 여부"),
                                fieldWithPath("size").description("페이지당 요소의 수"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                                fieldWithPath("empty").description("현재 페이지가 비어 있는지 여부")
                        )
                ));

    }


}
