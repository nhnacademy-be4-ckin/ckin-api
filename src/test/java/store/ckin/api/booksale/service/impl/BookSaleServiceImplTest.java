package store.ckin.api.booksale.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.book.entity.Book;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.repository.BookSaleRepository;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.service.PackagingService;
import store.ckin.api.sale.entity.Sale;

/**
 * 주문 도서 서비스 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@ExtendWith(MockitoExtension.class)
class BookSaleServiceImplTest {

    @InjectMocks
    BookSaleServiceImpl bookSaleService;

    @Mock
    BookSaleRepository bookSaleRepository;

    @Mock
    PackagingService packagingService;

    @Test
    @DisplayName("주문 도서 저장 테스트")
    void testCreateBookSale() {
        BookSaleCreateRequestDto firstDto = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(firstDto, "bookId", 1L);
        ReflectionTestUtils.setField(firstDto, "appliedCouponId", 1L);
        ReflectionTestUtils.setField(firstDto, "packagingId", 1L);
        ReflectionTestUtils.setField(firstDto, "quantity", 1);
        ReflectionTestUtils.setField(firstDto, "paymentAmount", 10000);

        BookSaleCreateRequestDto secondDto = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(secondDto, "bookId", 2L);
        ReflectionTestUtils.setField(secondDto, "appliedCouponId", null);
        ReflectionTestUtils.setField(secondDto, "packagingId", 0L);
        ReflectionTestUtils.setField(secondDto, "quantity", 3);
        ReflectionTestUtils.setField(secondDto, "paymentAmount", 45000);

        List<BookSaleCreateRequestDto> requestDtoList = List.of(firstDto, secondDto);

        given(packagingService.getPackagingPolicy(any()))
                .willReturn(PackagingResponseDto.builder()
                        .packagingId(1L)
                        .packagingPrice(3000)
                        .packagingType("선물용 포장")
                        .build());

        bookSaleService.createBookSale(1L, requestDtoList);

        verify(bookSaleRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("주문 도서 상태 변경 테스트")
    void testUpdateBookSaleState() {

        Sale firstSale = Sale.builder().saleId(1L).build();
        Sale secondSale = Sale.builder().saleId(2L).build();

        Book firstBook = Book.builder().bookId(1L).build();
        Book secondBook = Book.builder().bookId(2L).build();

        BookSale.Pk firstPk = new BookSale.Pk(1L, 1L);
        BookSale firstBookSale =
                new BookSale(
                        firstPk,
                        firstSale,
                        firstBook,
                        1L,
                        3,
                        3000,
                        "선물용 포장",
                        10000,
                        BookSale.BookSaleState.ORDER);

        BookSale.Pk secondPk = new BookSale.Pk(1L, 3L);
        BookSale secondBookSale =
                new BookSale(
                        secondPk,
                        secondSale,
                        secondBook,
                        null,
                        1,
                        3000,
                        "리본 포장",
                        15000,
                        BookSale.BookSaleState.ORDER);

        List<BookSale> bookSaleList = List.of(firstBookSale, secondBookSale);


        given(bookSaleRepository.findAllByPkSaleId(anyLong()))
                .willReturn(bookSaleList);

        bookSaleService.updateBookSaleState(1L, BookSale.BookSaleState.COMPLETE);

        verify(bookSaleRepository, times(1)).findAllByPkSaleId(anyLong());

        assertEquals(BookSale.BookSaleState.COMPLETE, firstBookSale.getBookSaleState());
        assertEquals(BookSale.BookSaleState.COMPLETE, secondBookSale.getBookSaleState());
    }
}