package store.ckin.api.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.file.entity.File;

import java.util.Optional;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 03. 03.
 */
public interface FileRepository extends JpaRepository<File, String>, FileRepositoryCustom {
    Optional<File> findByFileUrl(String fileUrl);

    Optional<File> findByBook_BookId(Long bookId);

}
