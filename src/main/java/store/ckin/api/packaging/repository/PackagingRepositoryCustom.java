package store.ckin.api.packaging.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * 포장 정책 Repository Querydsl 사용할 메서드가 있는 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 21.
 */
@NoRepositoryBean
public interface PackagingRepositoryCustom {

    boolean existsByType(String type);
}
