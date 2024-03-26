package store.ckin.api.sale.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 ID와 도서 ID를 통해 주문 리스트에 해당 주문이 존재하는지 확인하는 요청 DTO
 *
 * @author 정승조
 * @version 2024. 03. 26.
 */
@Getter
@NoArgsConstructor
public class SaleCheckByMemberRequestDto {

    private Long memberId;

    private Long bookId;
}
