package store.ckin.api.pointhistory.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;
import store.ckin.api.pointhistory.entity.PointHistory;
import store.ckin.api.pointhistory.repository.PointHistoryRepository;
import store.ckin.api.pointhistory.service.PointHistoryService;

/**
 * 포인트 내역 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createPointHistory(PointHistoryCreateRequestDto requestDto) {

        Long memberId = requestDto.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        PointHistory pointHistory = PointHistory.builder()
                .member(member)
                .pointHistoryReason(requestDto.getPointHistoryReason())
                .pointHistoryPoint(requestDto.getPointHistoryPoint())
                .pointHistoryTime(requestDto.getPointHistoryTime())
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<List<PointHistoryResponseDto>> getPointHistoryList(Long memberId, Pageable pageable) {
        Page<PointHistoryResponseDto> pointHistoryList = pointHistoryRepository.getPointHistoryList(memberId, pageable);
        PageInfo pageInfo = PageInfo.builder()
                .page(pointHistoryList.getNumber())
                .totalPages(pointHistoryList.getTotalPages())
                .totalElements((int) pointHistoryList.getTotalElements())
                .size(pointHistoryList.getSize())
                .build();

        return new PagedResponse<>(pointHistoryList.getContent(), pageInfo);
    }
}
