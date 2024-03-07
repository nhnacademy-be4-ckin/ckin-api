package store.ckin.api.booksale.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.repository.BookSaleRepository;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.service.PackagingService;

/**
 * 주문 도서 (리스트) 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Service
@RequiredArgsConstructor
public class BookSaleServiceImpl implements BookSaleService {

    private final BookSaleRepository bookSaleRepository;

    private final PackagingService packagingService;

    @Override
    @Transactional
    public void createBookSale(Long saleId, List<BookSaleCreateRequestDto> bookSaleList) {

        for (BookSaleCreateRequestDto bookSaleDto : bookSaleList) {

            PackagingResponseDto packagingPolicy;
            if (Objects.nonNull(bookSaleDto.getPackagingId())) {
                packagingPolicy = packagingService.getPackagingPolicy(bookSaleDto.getPackagingId());
            } else {
                packagingPolicy = PackagingResponseDto.builder()
                        .packagingPrice(0)
                        .build();
            }

            BookSale.Pk pk = new BookSale.Pk(saleId, bookSaleDto.getBookId());

            BookSale bookSale = BookSale.builder()
                    .pk(pk)
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
}
