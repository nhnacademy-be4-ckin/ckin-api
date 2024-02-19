package store.ckin.api.author.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import store.ckin.api.author.dto.response.AuthorResponseDto;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.repository.impl.AuthorRepositoryImpl;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 02. 16.
 */
@DataJpaTest
@Import(AuthorRepositoryImpl.class)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenFindById_thenReturnAuthor() {
        // given
        Author givenAuthor = Author.builder().authorName("테스트 작가").build();
        entityManager.persist(givenAuthor);
        entityManager.flush();

        // when
        Author foundAuthor = authorRepository.findById(givenAuthor.getAuthorId()).orElse(null);

        // then
        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getAuthorName()).isEqualTo("테스트 작가");
    }

    @Test
    public void whenSave_thenPersistAuthor() {
        // given
        Author givenAuthor = Author.builder().authorName("테스트 작가").build();

        // when
        Author savedAuthor = authorRepository.save(givenAuthor);

        // then
        Author foundAuthor = entityManager.find(Author.class, savedAuthor.getAuthorId());
        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getAuthorName()).isEqualTo("테스트 작가");
    }


    @Test
    public void whenFindAuthorsByName_thenReturnMatchingAuthors() {
        // given
        Author author1 = Author.builder().authorName("김인직").build();
        Author author2 = Author.builder().authorName("김인후").build();
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.flush();

        // when
        List<AuthorResponseDto> foundAuthors = authorRepository.findAuthorsByName("김");

        // then
        assertThat(foundAuthors).isNotEmpty();
        assertThat(foundAuthors.stream().anyMatch(a -> a.getAuthorName().contains("김"))).isTrue();
    }


}
