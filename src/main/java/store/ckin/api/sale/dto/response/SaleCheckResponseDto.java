package store.ckin.api.sale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문이 존재하는지 확인하는 응답 DTO
 *
 * @author 정승조
 * @version 2024. 03. 26.
 */

@Getter
@AllArgsConstructor
public class SaleCheckResponseDto {

    private Boolean isExist;
}
