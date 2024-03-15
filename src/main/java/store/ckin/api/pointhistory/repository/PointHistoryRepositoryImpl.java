package store.ckin.api.pointhistory.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;
import store.ckin.api.pointhistory.entity.PointHistory;
import store.ckin.api.pointhistory.entity.QPointHistory;

/**
 * 포인트 내역 레포지토리 Querydsl 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */
public class PointHistoryRepositoryImpl extends QuerydslRepositorySupport implements PointHistoryRepositoryCustom {

    public PointHistoryRepositoryImpl() {
        super(PointHistory.class);
    }

    @Override
    public Page<PointHistoryResponseDto> getPointHistoryList(Long memberId, Pageable pageable) {

        QPointHistory pointHistory = QPointHistory.pointHistory;

        List<PointHistoryResponseDto> pointHistoryList = from(pointHistory)
                .where(pointHistory.member.id.eq(memberId))
                .select(Projections.constructor(PointHistoryResponseDto.class,
                        pointHistory.pointHistoryId,
                        pointHistory.member.id,
                        pointHistory.pointHistoryReason,
                        pointHistory.pointHistoryPoint,
                        pointHistory.pointHistoryTime))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = from(pointHistory)
                .where(pointHistory.member.id.eq(memberId))
                .select(getBuilder().count());

        return PageableExecutionUtils.getPage(pointHistoryList, pageable, count::fetchCount);
    }
}
