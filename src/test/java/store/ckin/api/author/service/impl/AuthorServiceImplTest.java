package store.ckin.api.author.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.exception.AuthorNotFoundException;
import store.ckin.api.author.repository.AuthorRepository;

/**
 * AuthorServiceImplTest.
 *
 * @author 나국로
 * @version 2024. 02. 19.
 */
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;


    @Test
    @DisplayName("작가 생성 요청 시 작가 생성")
    void givenAuthorCreateRequest_whenCreateAuthor_thenAuthorCreated() {
        AuthorCreateRequestDto requestDto = new AuthorCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "authorName", "테스트 작가");
        Author mockAuthor = Author.builder()
                .authorId(1L)
                .authorName("테스트 작가")
                .build();
        when(authorRepository.save(any(Author.class))).thenReturn(mockAuthor);

        AuthorResponseDto result = authorService.createAuthor(requestDto);

        assertNotNull(result);
        assertEquals(mockAuthor.getAuthorName(), result.getAuthorName());
    }

    @Test
    @DisplayName("ID로 작가 조회 시 작가 반환")
    void givenAuthorId_whenFindAuthorById_thenAuthorFound() {
        Long authorId = 1L;
        Author author = Author.builder()
                .authorId(authorId)
                .authorName("한국 작가")
                .build();

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        AuthorResponseDto result = authorService.findAuthorById(authorId);

        assertNotNull(result);
        assertEquals("한국 작가", result.getAuthorName());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 작가 조회 시 예외 발생")
    void givenNonExistingAuthorId_whenFindAuthorById_thenThrowsAuthorNotFoundException() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.findAuthorById(authorId));
    }

    @Test
    @DisplayName("작가 정보 수정 요청 시 작가 정보 업데이트")
    void givenAuthorIdAndModifyRequest_whenUpdateAuthor_thenAuthorUpdated() {
        Long authorId = 1L;
        Author existingAuthor = Author.builder()
                .authorId(authorId)
                .authorName("기존 이름")
                .build();
        AuthorModifyRequestDto authorModifyRequestDto = new AuthorModifyRequestDto();
        ReflectionTestUtils.setField(authorModifyRequestDto, "authorName", "업데이트된 이름");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(Author.builder()
                .authorId(authorId)
                .authorName("업데이트된 이름")
                .build());

        AuthorResponseDto updatedAuthor = authorService.updateAuthor(authorId, authorModifyRequestDto);

        assertNotNull(updatedAuthor);
        assertEquals("업데이트된 이름", updatedAuthor.getAuthorName());
    }

    @Test
    @DisplayName("작가 ID로 작가 삭제")
    void givenAuthorId_whenDeleteAuthor_thenAuthorDeleted() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(new Author()));

        authorService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    @DisplayName("페이징 정보로 모든 작가 조회")
    void givenPageable_whenFindAllAuthors_thenReturnsPagedAuthors() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("authorName"));
        List<Author> authorsList = List.of(Author.builder()
                        .authorId(1L)
                        .authorName("작가1")
                        .build(),
                Author.builder()
                        .authorId(2L)
                        .authorName("작가2")
                        .build());
        Page<Author> authorsPage = new PageImpl<>(authorsList, pageable, authorsList.size());

        when(authorRepository.findAll(pageable)).thenReturn(authorsPage);

        Page<AuthorResponseDto> result = authorService.findAllAuthors(pageable);

        assertNotNull(result);
        assertEquals(authorsList.size(), result.getContent().size());

        for (int i = 0; i < authorsList.size(); i++) {
            assertEquals(authorsList.get(i).getAuthorName(), result.getContent().get(i).getAuthorName());
        }
    }


    @Test
    @DisplayName("이름으로 작가 검색 시 해당 작가 리스트 반환")
    void givenAuthorName_whenFindAuthorsByName_thenReturnsAuthorsList() {
        String name = "작가";
        List<AuthorResponseDto> mockResponse = List.of(
                new AuthorResponseDto(1L, "작가1"),
                new AuthorResponseDto(2L, "작가2")
        );
        when(authorRepository.findAuthorsByName(name)).thenReturn(mockResponse);

        List<AuthorResponseDto> result = authorService.findAuthorsByName(name);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("작가1", result.get(0).getAuthorName());
    }

    @Test
    @DisplayName("저자가 없을 때 삭제 시도시 예외 발생")
    void givenNonExistingAuthorId_whenDeleteAuthor_thenThrowAuthorNotFoundException() {
        Long nonExistingAuthorId = 1L;
        when(authorRepository.findById(nonExistingAuthorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> {
            authorService.deleteAuthor(nonExistingAuthorId);
        });
    }


}
