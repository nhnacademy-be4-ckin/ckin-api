package store.ckin.api.packaging.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;

/**
 * 포장 정책 Repository Querydsl 사용할 메서드가 있는 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 21.
 */
@NoRepositoryBean
public interface PackagingRepositoryCustom {

    boolean existsByType(String type);

    Optional<PackagingResponseDto> getPackagingById(Long packagingId);

    List<PackagingResponseDto> getPackgingList();
}
