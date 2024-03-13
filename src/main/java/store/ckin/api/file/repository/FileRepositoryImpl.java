package store.ckin.api.file.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.file.entity.File;
import store.ckin.api.file.entity.QFile;

import java.util.List;

/**
 * description:
 *
 * @author : gaeun
 * @version : 2024. 03. 13
 */
public class FileRepositoryImpl extends QuerydslRepositorySupport implements FileRepositoryCustom {
    public FileRepositoryImpl() {
        super(File.class);
    }

    QFile file = QFile.file;

    @Override
    public List<String> findFilePathByReviewId(Long reviewId) {
        return from(file)
                .where(file.review.reviewId.eq(reviewId))
                .select(file.fileUrl)
                .fetch();
    }
}
