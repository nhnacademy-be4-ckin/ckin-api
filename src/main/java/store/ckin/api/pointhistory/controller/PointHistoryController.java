package store.ckin.api.pointhistory.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;
import store.ckin.api.pointhistory.service.PointHistoryService;

/**
 * 포인트 내역 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point-history")
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    @PostMapping
    public ResponseEntity<Void> createPointHistory(@Valid @RequestBody PointHistoryCreateRequestDto requestDto) {

        pointHistoryService.createPointHistory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<PointHistoryResponseDto>>> getPointHistoryList(
            @RequestParam("memberId") Long memberId,
            @PageableDefault Pageable pageable) {

        return ResponseEntity.ok().body(pointHistoryService.getPointHistoryList(memberId, pageable));
    }
}
