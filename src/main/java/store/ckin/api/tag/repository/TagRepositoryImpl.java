package store.ckin.api.tag.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.tag.dto.response.TagResponseDto;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 19
 */
public class TagRepositoryImpl extends QuerydslRepositorySupport implements TagRepositoryCustom {
    public TagRepositoryImpl(Class<?> domainClass) {
        super(domainClass);
    }
}
