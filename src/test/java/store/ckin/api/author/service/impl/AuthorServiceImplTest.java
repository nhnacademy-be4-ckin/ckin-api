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
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;


    @Test
    void givenAuthorCreateRequest_whenCreateAuthor_thenAuthorCreated() {
        // given
        AuthorCreateRequestDto requestDto = new AuthorCreateRequestDto("테스트 작가");
        Author mockAuthor = new Author(1L, "테스트 작가");
        when(authorRepository.save(any(Author.class))).thenReturn(mockAuthor);

        // when
        AuthorResponseDto result = authorService.createAuthor(requestDto);

        // then
        assertNotNull(result);
        assertEquals(mockAuthor.getAuthorName(), result.getAuthorName());
    }

    @Test
    void givenAuthorId_whenFindAuthorById_thenAuthorFound() {
        Long authorId = 1L;
        Author author = new Author(authorId, "한국 작가");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        AuthorResponseDto result = authorService.findAuthorById(authorId);

        assertNotNull(result);
        assertEquals("한국 작가", result.getAuthorName());
    }

    @Test
    void givenNonExistingAuthorId_whenFindAuthorById_thenThrowsAuthorNotFoundException() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.findAuthorById(authorId));
    }

    @Test
    void givenAuthorIdAndModifyRequest_whenUpdateAuthor_thenAuthorUpdated() {
        Long authorId = 1L;
        Author existingAuthor = new Author(authorId, "기존 이름");
        AuthorModifyRequestDto dto = new AuthorModifyRequestDto("업데이트된 이름");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(new Author(authorId, "업데이트된 이름"));

        AuthorResponseDto updatedAuthor = authorService.updateAuthor(authorId, dto);

        assertNotNull(updatedAuthor);
        assertEquals("업데이트된 이름", updatedAuthor.getAuthorName());
    }

    @Test
    void givenAuthorId_whenDeleteAuthor_thenAuthorDeleted() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(new Author()));

        authorService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void givenPageable_whenFindAllAuthors_thenReturnsPagedAuthors() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("authorName"));
        List<Author> authorsList = List.of(new Author(1L, "작가1"), new Author(2L, "작가2"));
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


}
