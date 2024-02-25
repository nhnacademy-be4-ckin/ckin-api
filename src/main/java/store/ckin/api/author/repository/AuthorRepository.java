package store.ckin.api.author.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.author.entity.Author;

/**
 * AuthorRepository.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {

}
