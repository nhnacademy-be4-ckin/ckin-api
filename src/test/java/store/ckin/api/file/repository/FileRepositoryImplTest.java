package store.ckin.api.file.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.file.entity.File;
import store.ckin.api.review.entity.Review;

/**
 * FileRepositoryTest.
 *
 * @author 나국로
 * @version 2024. 03. 22.
 */
@DataJpaTest
class FileRepositoryImplTest {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("리뷰 ID에 해당하는 파일 경로 조회")
    void givenReviewId_whenFindFilePathByReviewId_thenReturnFilePaths() {
        Review review = new Review();
        entityManager.persist(review);

        File file1 = File.builder()
                .fileId("file1")
                .fileOriginName("origin1")
                .fileUrl("url1")
                .fileExtension("ext1")
                .fileCategory("category1")
                .review(review)
                .build();
        entityManager.persist(file1);

        File file2 = File.builder()
                .fileId("file2")
                .fileOriginName("origin2")
                .fileUrl("url2")
                .fileExtension("ext2")
                .fileCategory("category2")
                .review(review)
                .build();
        entityManager.persist(file2);

        entityManager.flush();
        entityManager.clear();

        Long reviewId = review.getReviewId();
        List<String> expectedFilePaths = List.of("url1", "url2");

        List<String> filePaths = fileRepository.findFilePathByReviewId(reviewId);

        assertEquals(expectedFilePaths, filePaths);
    }

}