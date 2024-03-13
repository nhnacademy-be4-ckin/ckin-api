package store.ckin.api.booksale.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.repository.BookSaleRepository;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.service.PackagingService;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

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

    @Mock
    SaleRepository saleRepository;

    @Mock
    BookRepository bookRepository;

    Book book;

    Sale sale;

    BookSaleCreateRequestDto firstDto;

    BookSaleCreateRequestDto secondDto;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .bookId(1L)
                .bookTitle("테스트 책")
                .bookRegularPrice(20000)
                .bookSalePrice(18000)
                .build();

        sale = Sale.builder()
                .saleId(1L)
                .saleNumber("1232421")
                .saleDeliveryFee(5000)
                .salePointUsage(0)
                .saleTotalPrice(23000)
                .build();

        firstDto = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(firstDto, "bookId", 1L);
        ReflectionTestUtils.setField(firstDto, "appliedCouponId", 1L);
        ReflectionTestUtils.setField(firstDto, "packagingId", 1L);
        ReflectionTestUtils.setField(firstDto, "quantity", 1);
        ReflectionTestUtils.setField(firstDto, "paymentAmount", 10000);

        secondDto = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(secondDto, "bookId", 2L);
        ReflectionTestUtils.setField(secondDto, "appliedCouponId", null);
        ReflectionTestUtils.setField(secondDto, "packagingId", 0L);
        ReflectionTestUtils.setField(secondDto, "quantity", 3);
        ReflectionTestUtils.setField(secondDto, "paymentAmount", 45000);
    }

    @Test
    @DisplayName("주문 도서 저장 - 실패(주문이 존재하지 않는 경우)")
    void testCreateBookSale_Fail_SaleNotFound() {
        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        List<BookSaleCreateRequestDto> bookSaleRequestDto = List.of(firstDto, secondDto);
        assertThrows(SaleNotFoundException.class,
                () -> bookSaleService.createBookSale(1L, bookSaleRequestDto));

        verify(saleRepository, times(1)).findById(anyLong());
        verify(packagingService, times(0)).getPackagingPolicy(any());
        verify(bookRepository, times(0)).findById(anyLong());
        verify(bookSaleRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("주문 도서 저장 - 실패(도서가 존재하지 않는 경우)")
    void testCreateBookSale_Fail_BookNotFound() {
        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        given(bookRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        List<BookSaleCreateRequestDto> bookSaleRequestDto = List.of(firstDto, secondDto);
        assertThrows(BookNotFoundException.class,
                () -> bookSaleService.createBookSale(1L, bookSaleRequestDto));

        verify(saleRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(packagingService, times(0)).getPackagingPolicy(any());
        verify(bookSaleRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("주문 도서 저장 - 성공")
    void testCreateBookSale_Success_Packaging() {


        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        given(bookRepository.findById(anyLong()))
                .willReturn(Optional.of(book));

        given(packagingService.getPackagingPolicy(any()))
                .willReturn(PackagingResponseDto.builder()
                        .packagingId(1L)
                        .packagingPrice(3000)
                        .packagingType("선물용 포장")
                        .build());

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        List<BookSaleCreateRequestDto> bookSaleRequestDto = List.of(firstDto, secondDto);

        bookSaleService.createBookSale(1L, bookSaleRequestDto);

        verify(saleRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(2)).findById(anyLong());
        verify(packagingService, times(1)).getPackagingPolicy(any());
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
                BookSale.builder()
                        .pk(firstPk)
                        .sale(firstSale)
                        .book(firstBook)
                        .couponId(1L)
                        .bookSaleQuantity(1)
                        .bookSalePackagingPrice(3000)
                        .bookSalePackagingType("선물용 포장")
                        .bookSalePaymentAmount(10000)
                        .bookSaleState(BookSale.BookSaleState.ORDER)
                        .build();

        BookSale.Pk secondPk = new BookSale.Pk(1L, 3L);
        BookSale secondBookSale =
                BookSale.builder()
                        .pk(secondPk)
                        .sale(secondSale)
                        .book(secondBook)
                        .couponId(null)
                        .bookSaleQuantity(3)
                        .bookSalePackagingPrice(0)
                        .bookSalePackagingType(null)
                        .bookSalePaymentAmount(45000)
                        .bookSaleState(BookSale.BookSaleState.ORDER)
                        .build();

        List<BookSale> bookSaleList = List.of(firstBookSale, secondBookSale);


        given(bookSaleRepository.findAllByPkSaleId(anyLong()))
                .willReturn(bookSaleList);

        bookSaleService.updateBookSaleState(1L, BookSale.BookSaleState.COMPLETE);

        verify(bookSaleRepository, times(1)).findAllByPkSaleId(anyLong());

        assertEquals(BookSale.BookSaleState.COMPLETE, firstBookSale.getBookSaleState());
        assertEquals(BookSale.BookSaleState.COMPLETE, secondBookSale.getBookSaleState());
    }
}