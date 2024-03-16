package store.ckin.api.category.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * description:
 *
 * @author : gaeun
 * @version : 2024. 03. 16
 */
@NoRepositoryBean
public interface CategoryRepositoryCustom {
    /**
     * 카테고리 아이디에 해당하는 부모 카테고리 아이디를 포함하여 반환합니다.
     *
     * @param categoryIds 카테고리 아이디 목록
     * @return 부모 카테고리 아이디 목록
     */
    List<Long> getParentIds(List<Long> categoryIds);
}
