package store.ckin.api.author.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
                .andDo(print());
    }


    @Test
    @DisplayName("작가 생성 요청 시 작가 생성")
    void givenAuthorCreateRequest_whenCreateAuthor_thenAuthorCreated() throws Exception {
        String name = "작가";
        List<AuthorResponseDto> mockAuthors = List.of(
                new AuthorResponseDto(1L, "작가1"),
                new AuthorResponseDto(2L, "작가2")
        );
        when(authorService.findAuthorsByName(name)).thenReturn(mockAuthors);

        mockMvc.perform(get("/api/authors/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(mockAuthors.size())))
                .andExpect(jsonPath("$[0].authorId", equalTo(1)))
                .andExpect(jsonPath("$[0].authorName", equalTo("작가1")))
                .andExpect(jsonPath("$[1].authorId", equalTo(2)))
                .andExpect(jsonPath("$[1].authorName", equalTo("작가2")))
                .andDo(print());
    }

    @Test
    @DisplayName("작가 정보 수정 요청 시 작가 정보 업데이트")
    void givenAuthorIdAndModifyRequest_whenUpdateAuthor_thenAuthorUpdated() throws Exception {
        AuthorCreateRequestDto createRequest = new AuthorCreateRequestDto("김작가");
        AuthorResponseDto createdAuthor = new AuthorResponseDto(1L, "김작가");
        when(authorService.createAuthor(any(AuthorCreateRequestDto.class))).thenReturn(createdAuthor);

        String json = new ObjectMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorName", equalTo("김작가")))
                .andDo(print());
    }

    @Test
    @DisplayName("작가 정보 업데이트")
    void updateAuthorTest() throws Exception {
        Long authorId = 1L;
        AuthorModifyRequestDto modifyRequest = new AuthorModifyRequestDto("김업뎃");
        AuthorResponseDto updatedAuthor = new AuthorResponseDto(authorId, "김업뎃");
        when(authorService.updateAuthor(eq(authorId), any(AuthorModifyRequestDto.class))).thenReturn(updatedAuthor);
        String json = new ObjectMapper().writeValueAsString(modifyRequest);

        mockMvc.perform(put("/api/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", equalTo("김업뎃")))
                .andDo(print());
    }

    @Test
    @DisplayName("작가 ID로 작가 삭제")
    void givenAuthorId_whenDeleteAuthor_thenAuthorDeleted() throws Exception {
        Long authorId = 1L;
        doNothing().when(authorService).deleteAuthor(authorId);

        mockMvc.perform(delete("/api/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("잘못된 입력 시 400 반환")
    void whenInvalidInput_thenReturns400() throws Exception {
        AuthorCreateRequestDto dto = new AuthorCreateRequestDto("");
        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("수정폼 잘못된 입력 시 400 반환")
    void whenInvalidInputForUpdate_thenReturns400() throws Exception {
        Long authorId = 1L;
        AuthorModifyRequestDto dto = new AuthorModifyRequestDto("");

        mockMvc.perform(put("/api/authors/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("생성 시 작가 이름이 너무 길면 400 반환")
    void whenAuthorNameTooLongInCreate_thenReturns400() throws Exception {
        String longName = "a".repeat(201);
        AuthorCreateRequestDto dto = new AuthorCreateRequestDto(longName);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("업데이트 시 작가 이름이 너무 길면 400 반환")
    void whenAuthorNameTooLongInUpdate_thenReturns400() throws Exception {
        Long authorId = 1L;
        String longName = "a".repeat(201);
        AuthorModifyRequestDto dto = new AuthorModifyRequestDto(longName);

        mockMvc.perform(put("/api/authors/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
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
                .andDo(print());
    }


}
