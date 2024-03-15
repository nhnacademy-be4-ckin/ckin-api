package store.ckin.api.file.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * description:
 *
 * @author : gaeun
 * @version : 2024. 03. 13
 */
@NoRepositoryBean
public interface FileRepositoryCustom {
    List<String> findFilePathByReviewId(Long reviewId);
}
