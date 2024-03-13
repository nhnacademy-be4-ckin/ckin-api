package store.ckin.api.booksale.service;

import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.entity.BookSale;

import java.util.List;

/**
 * 주문 도서 (리스트) 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */
public interface BookSaleService {

    /**
     * 주문 도서를 생성하는 메서드입니다.
     *
     * @param saleId       주문 ID
     * @param bookSaleList 주문 도서 리스트
     */
    void createBookSale(Long saleId, List<BookSaleCreateRequestDto> bookSaleList);

    /**
     * 주문 도서의 상태를 업데이트하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @param state  주문 도서 상태
     */
    void updateBookSaleState(Long saleId, BookSale.BookSaleState state);

    /**
     * 주문 ID를 통해 주문한 도서에 대한 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 주문 도서 상세 정보 응답 DTO 리스트
     */
    List<BookAndBookSaleResponseDto> getBookSaleDetail(Long saleId);
}
