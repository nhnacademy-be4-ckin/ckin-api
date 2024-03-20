package store.ckin.api.pointhistory.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;

/**
 * 포인트 내역 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
public interface PointHistoryService {

    void createPointHistory(PointHistoryCreateRequestDto requestDto);

    PagedResponse<List<PointHistoryResponseDto>> getPointHistoryList(Long memberId, Pageable pageable);
}
