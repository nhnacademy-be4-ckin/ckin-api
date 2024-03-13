package store.ckin.api.booksale.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.repository.BookSaleRepository;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.service.PackagingService;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

/**
 * 주문 도서 (리스트) 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */


@Service
@RequiredArgsConstructor
public class BookSaleServiceImpl implements BookSaleService {

    private final BookRepository bookRepository;

    private final SaleRepository saleRepository;

    private final BookSaleRepository bookSaleRepository;

    private final PackagingService packagingService;

    @Override
    @Transactional
    public void createBookSale(Long saleId, List<BookSaleCreateRequestDto> bookSaleList) {

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        for (BookSaleCreateRequestDto bookSaleDto : bookSaleList) {

            Book book = bookRepository.findById(bookSaleDto.getBookId())
                    .orElseThrow(() -> new BookNotFoundException(bookSaleDto.getBookId()));

            PackagingResponseDto packagingPolicy;
            if (bookSaleDto.getPackagingId() > 0) {
                packagingPolicy = packagingService.getPackagingPolicy(bookSaleDto.getPackagingId());
            } else {
                packagingPolicy = PackagingResponseDto.builder()
                        .packagingPrice(0)
                        .build();
            }

            BookSale.Pk pk = new BookSale.Pk(sale.getSaleId(), book.getBookId());

            BookSale bookSale = BookSale.builder()
                    .pk(pk)
                    .sale(sale)
                    .book(book)
                    .couponId(bookSaleDto.getAppliedCouponId())
                    .bookSaleQuantity(bookSaleDto.getQuantity())
                    .bookSalePackagingPrice(packagingPolicy.getPackagingPrice())
                    .bookSalePackagingType(packagingPolicy.getPackagingType())
                    .bookSalePaymentAmount(bookSaleDto.getPaymentAmount())
                    .bookSaleState(BookSale.BookSaleState.ORDER)
                    .build();

            bookSaleRepository.save(bookSale);
        }
    }

    @Override
    @Transactional
    public void updateBookSaleState(Long saleId, BookSale.BookSaleState state) {
        bookSaleRepository.findAllByPkSaleId(saleId)
                .forEach(bookSale -> bookSale.updateBookSaleState(state));
    }

    @Override
    public List<BookAndBookSaleResponseDto> getBookSaleDetail(Long saleId) {
        return bookSaleRepository.getBookSaleDetailBySaleId(saleId);
    }
}
