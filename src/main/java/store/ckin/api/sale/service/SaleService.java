package store.ckin.api.sale.service;

import java.util.List;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;

/**
 * 주문 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */
public interface SaleService {

    Long createSale(SaleCreateNoBookRequestDto requestDto);

    List<SaleResponseDto> getSales();
}
