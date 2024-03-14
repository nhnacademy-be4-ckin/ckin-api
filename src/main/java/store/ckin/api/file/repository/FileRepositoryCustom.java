package store.ckin.api.file.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

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
