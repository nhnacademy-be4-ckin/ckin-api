package store.ckin.api.pointpolicy.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * 포인트 정책 Repository Querydsl 사용할 메서드가 있는 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@NoRepositoryBean
public interface PointPolicyRepositoryCustom {

    /**
     * 포인트 정책의 ID와 이름을 통해 존재 여부를 파악하는 메서드입니다.
     *
     * @param id   포인트 정책 ID
     * @param name 포인트 정책 이릠
     * @return 포인트 정책 존재 여부
     */
    boolean existsPointPolicy(Long id, String name);
}
