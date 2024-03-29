package store.ckin.api.tag.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.tag.entity.Tag;

/**
 * 태그 레포지토리 인터페이스
 *
 * @author : 김준현
 * @version : 2024. 02. 14
 */

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    Page<Tag> findAllByOrderByTagId(Pageable pageable);

    boolean existsByTagName(String tagName);
}
