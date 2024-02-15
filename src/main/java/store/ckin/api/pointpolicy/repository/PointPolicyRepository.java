package store.ckin.api.pointpolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.pointpolicy.entity.PointPolicy;

/**
 * 포인트 정책을 관리하는 레포지토리입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */

public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long>, PointPolicyRepositoryCustom {

}
