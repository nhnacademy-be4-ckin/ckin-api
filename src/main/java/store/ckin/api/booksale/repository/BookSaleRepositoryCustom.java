package store.ckin.api.booksale.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;

import java.util.List;

/**
 * 도서 주문 리스트 레포지토리 커스텀 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */

@NoRepositoryBean
public interface BookSaleRepositoryCustom {

    List<BookAndBookSaleResponseDto> getBookSaleDetailBySaleId(Long saleId);
}
