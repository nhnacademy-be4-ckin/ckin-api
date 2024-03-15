package store.ckin.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.review.entity.Review;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 03. 03.
 */
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
}
