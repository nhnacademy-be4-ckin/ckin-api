package store.ckin.api.packaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.packaging.entity.Packaging;

/**
 * 포장지 정책 레포지토리입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */
public interface PackagingRepository extends JpaRepository<Packaging, Long> {
}
