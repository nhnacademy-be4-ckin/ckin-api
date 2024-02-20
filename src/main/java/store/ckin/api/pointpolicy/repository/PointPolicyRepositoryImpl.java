package store.ckin.api.pointpolicy.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.entity.QPointPolicy;

/**
 * 포인트 정책 Repository Querydsl 사용을 위한 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */
public class PointPolicyRepositoryImpl extends QuerydslRepositorySupport implements PointPolicyRepositoryCustom {

    public PointPolicyRepositoryImpl() {
        super(PointPolicy.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param id   포인트 정책 ID
     * @param name 포인트 정책 이릠
     * @return 포인트 정책 존재 여부
     */
    @Override
    public boolean existsPointPolicy(Long id, String name) {

        QPointPolicy pointPolicy = QPointPolicy.pointPolicy;

        return from(pointPolicy)
                .where(pointPolicy.pointPolicyId.eq(id)
                        .or(pointPolicy.pointPolicyName.eq(name)))
                .fetchCount() > 0;

    }
}
