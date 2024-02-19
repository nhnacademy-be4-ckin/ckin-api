package store.ckin.api.tag.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.entity.Tag;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 17
 */

@DataJpaTest
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        // given
        Tag testTag1 = Tag.builder()
                .tagName("테스트 태그1")
                .build();
        Tag testTag2 = Tag.builder()
                .tagName("테스트 태그2")
                .build();

        tagRepository.save(testTag1);
        tagRepository.save(testTag2);
    }

    @Test
    @DisplayName("태그 전체 조회")
    void findAllTagsTest() {
        // when
        List<TagResponseDto> actual = tagRepository.findAllTags();

        // then
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("태그 이름 존재 여부 테스트")
    void existByTagNameTest() {
        // then
        assertTrue(tagRepository.existsByTagName("테스트 태그1"));
        assertTrue(tagRepository.existsByTagName("테스트 태그2"));
        assertFalse(tagRepository.existsByTagName("존재하지 않는 태그"));
    }
}