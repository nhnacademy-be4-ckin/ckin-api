package store.ckin.api.sale.repository;

import java.util.List;
import store.ckin.api.sale.dto.response.SaleResponseDto;

/**
 * 주문 Repository Custom 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 03.
 */
public interface SaleRepositoryCustom {

    List<SaleResponseDto> findAllOrderByIdDesc();
}
