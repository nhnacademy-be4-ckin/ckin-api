package store.ckin.api.sale.facade;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.service.SaleService;

/**
 * 주문 퍼사드 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@ExtendWith(MockitoExtension.class)
class SaleFacadeTest {

    @InjectMocks
    SaleFacade saleFacade;

    @Mock
    SaleService saleService;

    @Mock
    BookSaleService bookSaleService;

    @Mock
    MemberService memberService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("주문 생성 테스트")
    void testCreateSale() {
        // given
        BookSaleCreateRequestDto firstDto = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(firstDto, "bookId", 1L);
        ReflectionTestUtils.setField(firstDto, "appliedCouponId", 1L);
        ReflectionTestUtils.setField(firstDto, "packagingId", 1L);
        ReflectionTestUtils.setField(firstDto, "quantity", 1);
        ReflectionTestUtils.setField(firstDto, "paymentAmount", 10000);

        BookSaleCreateRequestDto secondDto = new BookSaleCreateRequestDto();
        ReflectionTestUtils.setField(secondDto, "bookId", 2L);
        ReflectionTestUtils.setField(secondDto, "appliedCouponId", null);
        ReflectionTestUtils.setField(secondDto, "packagingId", null);
        ReflectionTestUtils.setField(secondDto, "quantity", 3);
        ReflectionTestUtils.setField(secondDto, "paymentAmount", 45000);

        List<BookSaleCreateRequestDto> bookSaleList = List.of(firstDto, secondDto);

        SaleCreateRequestDto requestDto = new SaleCreateRequestDto(
                1L,
                "정승조",
                "01012345678",
                "정승조",
                "01012345678",
                3000,
                LocalDate.of(2024, 3, 7),
                "123456",
                "광주광역시 동구 조선대 5길 ",
                "IT 융합대학",
                0,
                10000
        );

        ReflectionTestUtils.setField(requestDto, "bookSaleList", bookSaleList);


        // when
        saleFacade.createSale(requestDto);

        // then
        verify(saleService, times(1)).createSale(any());
        verify(bookSaleService, times(1)).createBookSale(anyLong(), any());
        verify(memberService, times(1)).updatePoint(anyLong(), any());
    }

    @Test
    @DisplayName("페이징 주문 목록 조회 테스트")
    void testGetSales() {

        saleFacade.getSales(Pageable.ofSize(10));

        verify(saleService, times(1)).getSales(Pageable.ofSize(10));
    }

    @Test
    @DisplayName("주문 상세 정보 조회 테스트")
    void testGetSaleDetail() {

        saleFacade.getSaleDetail(1L);

        verify(saleService, times(1)).getSaleDetail(1L);
    }

    @Test
    @DisplayName("주문 결제 상태 완료 변경 테스트")
    void testUpdateSalePaymentPaidStatus() {

        saleFacade.updateSalePaymentPaidStatus(1L);

        verify(saleService, times(1)).updateSalePaymentPaidStatus(1L);
    }

}