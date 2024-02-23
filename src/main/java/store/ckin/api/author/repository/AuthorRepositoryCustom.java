package store.ckin.api.author.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.author.dto.response.AuthorResponseDto;

/**
 * AuthorRepositoryCustom.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@NoRepositoryBean
public interface AuthorRepositoryCustom {
    /**
     * Find authors by name.
     *
     * @param name the name
     * @return the list
     */
    List<AuthorResponseDto> findAuthorsByName(String name);
}
