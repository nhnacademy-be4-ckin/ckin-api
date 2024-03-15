package store.ckin.api.pointhistory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;

/**
 * 포인트 내역 레포지토리 커스텀 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
public interface PointHistoryRepositoryCustom {

    Page<PointHistoryResponseDto> getPointHistoryList(Long memberId, Pageable pageable);
}
