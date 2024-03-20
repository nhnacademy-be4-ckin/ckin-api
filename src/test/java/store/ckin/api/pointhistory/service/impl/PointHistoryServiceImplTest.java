package store.ckin.api.pointhistory.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;
import store.ckin.api.pointhistory.repository.PointHistoryRepository;

/**
 * 포인트 내역 서비스 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 16.
 */

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceImplTest {

    @InjectMocks
    PointHistoryServiceImpl pointHistoryService;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("포인트 내역 생성 테스트")
    void testCreatePointHistory_Success() {
        PointHistoryCreateRequestDto pointHistory = new PointHistoryCreateRequestDto(1L, "회원가입", 5000, LocalDate.now());

        Member member = Member.builder().id(1L).build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        pointHistoryService.createPointHistory(pointHistory);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(pointHistoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("포인트 내역 생성 테스트 - 실패 (회원이 존재하지 않는 경우)")
    void testCreatePointHistory_Fail() {
        PointHistoryCreateRequestDto pointHistory = new PointHistoryCreateRequestDto(1L, "회원가입", 5000, LocalDate.now());

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        Assertions.assertThrows(MemberNotFoundException.class,
                () -> pointHistoryService.createPointHistory(pointHistory));

        verify(memberRepository, times(1)).findById(anyLong());
        verify(pointHistoryRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("포인트 내역 리스트 조회 테스트")
    void testGetPointHistoryList() {

        PointHistoryResponseDto pointHistory
                = new PointHistoryResponseDto(1L, 1L, "회원가입", 5000, LocalDate.now());

        Page<PointHistoryResponseDto> pointHistoryList = new PageImpl<>(List.of(pointHistory));

        given(pointHistoryRepository.getPointHistoryList(anyLong(), any()))
                .willReturn(pointHistoryList);

        Pageable pageable = Pageable.ofSize(10);

        PagedResponse<List<PointHistoryResponseDto>> actual =
                pointHistoryService.getPointHistoryList(1L, pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(pointHistoryList.getContent(), actual.getData()),
                () -> Assertions.assertEquals(pointHistoryList.getNumber(), actual.getPageInfo().getPage()),
                () -> Assertions.assertEquals(pointHistoryList.getTotalPages(), actual.getPageInfo().getTotalPages()),
                () -> Assertions.assertEquals((int) pointHistoryList.getTotalElements(), actual.getPageInfo().getTotalElements()),
                () -> Assertions.assertEquals(pointHistoryList.getSize(), actual.getPageInfo().getSize())
        );
    }

}