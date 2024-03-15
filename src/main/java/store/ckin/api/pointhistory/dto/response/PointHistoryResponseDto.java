package store.ckin.api.pointhistory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 포인트 내역 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */

@Getter
@AllArgsConstructor
public class PointHistoryResponseDto {

    private Long id;
    private Long memberId;
    private String pointHistoryReason;
    private Integer pointHistoryPoint;
    private String pointHistoryTime;

}
