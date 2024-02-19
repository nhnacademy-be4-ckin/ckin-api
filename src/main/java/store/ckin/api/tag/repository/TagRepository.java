package store.ckin.api.tag.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.entity.Tag;

/**
 * 태그 레포지토리 인터페이스
 *
 * @author : 김준현
 * @version : 2024. 02. 14
 */

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select new store.ckin.api.tag.dto.response.TagResponseDto(t.tagId, t.tagName) from Tag t")
    List<TagResponseDto> findAllTags();

    boolean existsByTagName(String tagName);
}
