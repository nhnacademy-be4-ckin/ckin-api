package store.ckin.api.sale.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문 정보 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Getter
@AllArgsConstructor
public class SaleInfoResponseDto {

    private String saleTitle;

    private String saleNumber;

    private String memberEmail;

    private String saleOrdererName;

    private String saleOrdererContact;

    private Integer totalPrice;

    private LocalDateTime saleDate;
}
