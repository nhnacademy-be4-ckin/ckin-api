package store.ckin.api.author.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.repository.impl.AuthorRepositoryImpl;

/**
 * AuthorRepositoryTest.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
@DataJpaTest
@Import(AuthorRepositoryImpl.class)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("ID로 작가 조회")
    void whenFindById_thenReturnAuthor() {
        Author givenAuthor = Author.builder().authorName("테스트 작가").build();
        entityManager.persist(givenAuthor);
        entityManager.flush();

        Author foundAuthor = authorRepository.findById(givenAuthor.getAuthorId()).orElse(null);

        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getAuthorName()).isEqualTo("테스트 작가");
    }

    @Test
    @DisplayName("작가 저장 확인")
    void whenSave_thenPersistAuthor() {
        Author givenAuthor = Author.builder().authorName("테스트 작가").build();

        Author savedAuthor = authorRepository.save(givenAuthor);

        Author foundAuthor = entityManager.find(Author.class, savedAuthor.getAuthorId());
        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getAuthorName()).isEqualTo("테스트 작가");
    }


    @Test
    @DisplayName("이름으로 작가 검색")
    void whenFindAuthorsByName_thenReturnMatchingAuthors() {
        Author author1 = Author.builder().authorName("김인직").build();
        Author author2 = Author.builder().authorName("김인후").build();
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.flush();
        Pageable pageable = PageRequest.of(0, 2);
        Page<AuthorResponseDto> foundAuthors = authorRepository.findAuthorsByName("김", pageable);

        assertThat(foundAuthors).isNotEmpty();
        assertThat(foundAuthors.stream().anyMatch(a -> a.getAuthorName().contains("김"))).isTrue();
    }


}
