package store.ckin.api.pointhistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.pointhistory.entity.PointHistory;

/**
 * PointHistory 레포지토리입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, PointHistoryRepositoryCustom {
}
