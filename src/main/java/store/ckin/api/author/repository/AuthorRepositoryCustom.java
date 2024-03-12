package store.ckin.api.author.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.author.dto.response.AuthorResponseDto;

/**
 * AuthorRepositoryCustom.
 *
 * @author 나국로
 * @version 2024. 02. 15.
 */
@NoRepositoryBean
public interface AuthorRepositoryCustom {
    /**
     * 이름으로 작가를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param name 작가의 이름
     * @param pageable 페이징 정보
     * @return 이름으로 검색된 작가 목록에 대한 페이지 객체
     */
    Page<AuthorResponseDto> findAuthorsByName(String name, Pageable pageable);
}
