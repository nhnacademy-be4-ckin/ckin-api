package store.ckin.api.booksale.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.ckin.api.booksale.entity.BookSale;

/**
 * 주문 도서 레포지토리 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@DataJpaTest
class BookSaleRepositoryTest {

    @Autowired
    BookSaleRepository bookSaleRepository;


    @Test
    @DisplayName("주문 도서 저장 테스트")
    void testSaveBookSale() {

        BookSale.Pk pk = new BookSale.Pk(1L, 1L);
        BookSale bookSale = BookSale.builder()
                .pk(pk)
                .couponId(1L)
                .bookSaleQuantity(30)
                .bookSalePackagingPrice(3000)
                .bookSalePackagingType("꽃무늬 포장")
                .bookSalePaymentAmount(8000)
                .bookSaleState(BookSale.BookSaleState.ORDER)
                .build();

        BookSale actual = bookSaleRepository.save(bookSale);

        assertAll(
                () -> assertEquals(bookSale.getPk(), actual.getPk()),
                () -> assertEquals(bookSale.getCouponId(), actual.getCouponId()),
                () -> assertEquals(bookSale.getBookSaleQuantity(), actual.getBookSaleQuantity()),
                () -> assertEquals(bookSale.getBookSalePackagingPrice(), actual.getBookSalePackagingPrice()),
                () -> assertEquals(bookSale.getBookSalePackagingType(), actual.getBookSalePackagingType()),
                () -> assertEquals(bookSale.getBookSalePaymentAmount(), actual.getBookSalePaymentAmount()),
                () -> assertEquals(bookSale.getBookSaleState(), actual.getBookSaleState())
        );
    }

    @Test
    @DisplayName("주문 ID를 통한 주문 도서 조회 테스트")
    void testFindAllByPkSaleId() {

            BookSale.Pk pk = new BookSale.Pk(1L, 1L);
            BookSale bookSale = BookSale.builder()
                    .pk(pk)
                    .couponId(1L)
                    .bookSaleQuantity(30)
                    .bookSalePackagingPrice(3000)
                    .bookSalePackagingType("꽃무늬 포장")
                    .bookSalePaymentAmount(8000)
                    .bookSaleState(BookSale.BookSaleState.ORDER)
                    .build();

            bookSaleRepository.save(bookSale);

            BookSale.Pk pk2 = new BookSale.Pk(1L, 2L);
            BookSale bookSale2 = BookSale.builder()
                    .pk(pk2)
                    .couponId(1L)
                    .bookSaleQuantity(30)
                    .bookSalePackagingPrice(3000)
                    .bookSalePackagingType("꽃무늬 포장")
                    .bookSalePaymentAmount(8000)
                    .bookSaleState(BookSale.BookSaleState.ORDER)
                    .build();

            bookSaleRepository.save(bookSale2);

            BookSale.Pk pk3 = new BookSale.Pk(2L, 1L);
            BookSale bookSale3 = BookSale.builder()
                    .pk(pk3)
                    .couponId(1L)
                    .bookSaleQuantity(30)
                    .bookSalePackagingPrice(3000)
                    .bookSalePackagingType("꽃무늬 포장")
                    .bookSalePaymentAmount(8000)
                    .bookSaleState(BookSale.BookSaleState.ORDER)
                    .build();

            bookSaleRepository.save(bookSale3);

            assertEquals(2, bookSaleRepository.findAllByPkSaleId(1L).size());
    }

}