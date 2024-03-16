package store.ckin.api.pointhistory.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 내역 생성 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryCreateRequestDto {

    @NotNull(message = "회원 아이디는 필수입니다.")
    private Long memberId;

    @NotBlank(message = "포인트 내역 사유를 입력해주세요.")
    @Size(max = 50, message = "포인트 내역 사유는 50자 이내로 입력해주세요.")
    private String pointHistoryReason;

    @NotNull(message = "포인트를 입력해주세요.")
    private Integer pointHistoryPoint;

    @NotNull(message = "기록하신 시간을 입력해주세요.")
    private LocalDate pointHistoryTime;
}
