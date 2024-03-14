package store.ckin.api.pointpolicy.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.entity.QPointPolicy;

import java.util.List;
import java.util.Optional;

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

    /**
     * {@inheritDoc}
     *
     * @param id 포인트 정책 ID
     * @return 조회된 포인트 정책 응답 DTO
     */
    @Override
    public Optional<PointPolicyResponseDto> getPointPolicyById(Long id) {

        QPointPolicy pointPolicy = QPointPolicy.pointPolicy;

        return Optional.of(from(pointPolicy)
                .where(pointPolicy.pointPolicyId.eq(id))
                .select(Projections.constructor(PointPolicyResponseDto.class,
                        pointPolicy.pointPolicyId,
                        pointPolicy.pointPolicyName,
                        pointPolicy.pointPolicyReserve))
                .fetchOne());
    }

    @Override
    public List<PointPolicyResponseDto> getPointPolicies() {

        QPointPolicy pointPolicy = QPointPolicy.pointPolicy;

        return from(pointPolicy)
                .select(Projections.constructor(PointPolicyResponseDto.class,
                        pointPolicy.pointPolicyId,
                        pointPolicy.pointPolicyName,
                        pointPolicy.pointPolicyReserve))
                .fetch();
    }


}
