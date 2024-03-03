package store.ckin.api.booksale.service;

import java.util.List;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;

/**
 * 주문 도서 (리스트) 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */
public interface BookSaleService {
    void createBookSale(Long saleId, List<BookSaleCreateRequestDto> bookSaleList);
}
