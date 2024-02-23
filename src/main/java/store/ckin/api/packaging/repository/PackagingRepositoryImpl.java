package store.ckin.api.packaging.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.entity.Packaging;
import store.ckin.api.packaging.entity.QPackaging;

/**
 * 포장 정책 Repository Querydsl 사용을 위한 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 21.
 */
public class PackagingRepositoryImpl extends QuerydslRepositorySupport implements PackagingRepositoryCustom {

    public PackagingRepositoryImpl() {
        super(Packaging.class);
    }

    @Override
    public boolean existsByType(String type) {

        QPackaging packaging = QPackaging.packaging;

        return from(packaging)
                .where(packaging.packagingType.eq(type))
                .fetchCount() > 0;
    }

    @Override
    public Optional<PackagingResponseDto> getPackagingById(Long packagingId) {

        QPackaging packaging = QPackaging.packaging;
        return Optional.of(from(packaging)
                .where(packaging.packagingId.eq(packagingId))
                .select(Projections.constructor(PackagingResponseDto.class,
                        packaging.packagingId,
                        packaging.packagingType,
                        packaging.packagingPrice))
                .fetchOne());
    }

    @Override
    public List<PackagingResponseDto> getAllPackaging() {

        QPackaging packaging = QPackaging.packaging;
        return from(packaging)
                .select(Projections.constructor(PackagingResponseDto.class,
                        packaging.packagingId,
                        packaging.packagingType,
                        packaging.packagingPrice))
                .fetch();
    }
}
