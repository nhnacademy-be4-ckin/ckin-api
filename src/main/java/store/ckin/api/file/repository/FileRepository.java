package store.ckin.api.file.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.file.entity.File;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 03. 03.
 */
public interface FileRepository extends JpaRepository<File, String> {
    Optional<File> findByFileUrl(String fileUrl);

    Optional<File> findByBook_BookId(Long bookId);

    List<File> findAllByReview_ReviewId(Long reviewId);
}
