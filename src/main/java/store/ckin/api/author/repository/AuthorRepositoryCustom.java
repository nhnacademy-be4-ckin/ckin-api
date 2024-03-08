package store.ckin.api.author.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<AuthorResponseDto> findAuthorsByName(String name, Pageable pageable);
}
