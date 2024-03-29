package store.ckin.api.sale.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.entity.PaymentStatus;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.pointhistory.service.PointHistoryService;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.request.SaleDeliveryUpdateRequestDto;
import store.ckin.api.sale.dto.response.SaleCheckResponseDto;
import store.ckin.api.sale.dto.response.SaleDetailResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.DeliveryStatus;
import store.ckin.api.sale.entity.SalePaymentStatus;
import store.ckin.api.sale.exception.SaleMemberNotMatchException;
import store.ckin.api.sale.exception.SaleOrdererContactNotMatchException;
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

    @Mock
    PaymentService paymentService;

    @Mock
    PointHistoryService pointHistoryService;

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

        SaleCreateRequestDto requestDto = new SaleCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "memberId", 1L);
        ReflectionTestUtils.setField(requestDto, "saleTitle", "테스트 제목");
        ReflectionTestUtils.setField(requestDto, "saleOrdererName", "정승조");
        ReflectionTestUtils.setField(requestDto, "saleOrdererContact", "01012345678");
        ReflectionTestUtils.setField(requestDto, "saleReceiverName", "정승조");
        ReflectionTestUtils.setField(requestDto, "saleReceiverContact", "01012345678");
        ReflectionTestUtils.setField(requestDto, "deliveryFee", 3000);
        ReflectionTestUtils.setField(requestDto, "saleDeliveryDate", LocalDate.of(2024, 3, 7));
        ReflectionTestUtils.setField(requestDto, "postcode", "123456");
        ReflectionTestUtils.setField(requestDto, "address", "광주광역시 동구 조선대 5길 ");
        ReflectionTestUtils.setField(requestDto, "detailAddress", "IT 융합대학");
        ReflectionTestUtils.setField(requestDto, "pointUsage", 300);
        ReflectionTestUtils.setField(requestDto, "totalPrice", 10000);
        ReflectionTestUtils.setField(requestDto, "bookSaleList", bookSaleList);

        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.createSale(any()))
                .willReturn(sale);

        // when
        String saleNumber = saleFacade.createSale(requestDto);

        // then
        assertEquals(sale.getSaleNumber(), saleNumber);
        verify(saleService, times(1)).createSale(any());
        verify(bookSaleService, times(1)).createBookSale(anyLong(), any());
        verify(memberService, times(1)).updatePoint(anyLong(), any());
        verify(pointHistoryService, times(1)).createPointHistory(any());
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


        BookAndBookSaleResponseDto bookSale =
                new BookAndBookSaleResponseDto(
                        1L,
                        "testimg.com",
                        "홍길동전",
                        5,
                        3L,
                        "A 포장",
                        1000,
                        50000);

        given(bookSaleService.getBookSaleDetail(anyLong()))
                .willReturn(List.of(bookSale));

        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleDetail(anyLong()))
                .willReturn(sale);


        PaymentResponseDto payment =
                new PaymentResponseDto(
                        1L,
                        3L,
                        "12421312",
                        PaymentStatus.DONE,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(10),
                        "test.com"
                );

        given(paymentService.getPayment(anyLong()))
                .willReturn(payment);

        SaleDetailResponseDto saleDetail = saleFacade.getSaleDetail(1L);

        assertAll(
                () -> assertEquals(saleDetail.getSaleResponseDto(), sale),
                () -> assertEquals(saleDetail.getBookSaleList().get(0), bookSale),
                () -> assertEquals(saleDetail.getPaymentResponseDto(), payment));

        verify(saleService, times(1)).getSaleDetail(1L);
    }

    @Test
    @DisplayName("주문 번호로 주문 상세 정보와 주문한 책 정보 조회 테스트")
    void testGetSaleWithBookResponse() {

        SaleWithBookResponseDto responseDto = new SaleWithBookResponseDto(
                "홍길동전",
                1L,
                "ABC1234DEF",
                "test@test.com",
                "Tester",
                "01012341234",
                "Tester",
                "01011112222",
                3000,
                LocalDate.of(2024, 3, 7).plusDays(1),
                LocalDateTime.of(2024, 3, 7, 12, 0),
                "12345",
                "광주광역시 동구 조선대 5길",
                0,
                10000
        );


        given(saleService.getSaleWithBook(anyString()))
                .willReturn(responseDto);

        SaleWithBookResponseDto actual = saleFacade.getSaleWithBookResponseDto("flskajvlkc12312");


        assertEquals(responseDto, actual);
        verify(saleService, times(1)).getSaleWithBook("flskajvlkc12312");
    }

    @Test
    @DisplayName("주문 결제 정보 조회 테스트")
    void testGetSalePaymentInfo() {

        saleFacade.getSalePaymentInfo("123456");

        verify(saleService, times(1)).getSalePaymentInfo("123456");
    }

    @Test
    @DisplayName("주문 번호로 주문 상세 조회 테스트 - 실패 (주문자 전화번호와 넘겨받은 전화번호가 다른 경우)")
    void testGetSaleDetailBySaleNumber_Fail_Contact_Different() {

        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0),
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0).plusDays(1),
                        LocalDate.of(2024, 3, 7).plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleBySaleNumber(anyString()))
                .willReturn(sale);

        assertThrows(SaleOrdererContactNotMatchException.class,
                () -> saleFacade.getGuestSaleDetailBySaleNumber("1234", "010111111111"));
    }

    @Test
    @DisplayName("주문 번호로 주문 상세 조회 테스트 - 성공")
    void testGetSaleDetailBySaleNumber_Success() {
        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0),
                        LocalDateTime.of(2024, 3, 7, 12, 0, 0).plusDays(1),
                        LocalDate.of(2024, 3, 7).plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleBySaleNumber(anyString()))
                .willReturn(sale);

        SaleDetailResponseDto actual = saleFacade.getGuestSaleDetailBySaleNumber("1234", "01012345678");

        assertEquals(sale, actual.getSaleResponseDto());
    }

    @Test
    @DisplayName("회원 ID로 회원의 모든 주문 내역 조회")
    void testGetSalesByMemberId() {
        saleFacade.getSalesByMemberId(1L, Pageable.ofSize(10));
        verify(saleService, times(1)).getSalesByMemberId(1L, Pageable.ofSize(10));
    }

    @Test
    @DisplayName("회원 ID와 주문 번호를 통해 주문 상세 정보 조회 - 실패 (회원 정보가 다른 경우)")
    void testGetMemberSaleDetailBySaleNumber_Fail_NotMatchException() {
        SaleResponseDto saleResponseDto =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        15000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleBySaleNumber(anyString()))
                .willReturn(saleResponseDto);

        assertThrows(SaleMemberNotMatchException.class,
                () -> saleFacade.getMemberSaleDetailBySaleNumber("1234", 5L));

        verify(saleService, times(1)).getSaleBySaleNumber(anyString());
        verify(bookSaleService, times(0)).getBookSaleDetail(anyLong());
        verify(paymentService, times(0)).getPayment(anyLong());
    }

    @Test
    @DisplayName("회원 ID와 주문 번호를 통해 주문 상세 정보 조회 - 성공")
    void testGetMemberSaleDetailBySaleNumber_Success() {
        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        15000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleBySaleNumber(anyString()))
                .willReturn(sale);

        BookAndBookSaleResponseDto bookSale =
                new BookAndBookSaleResponseDto(
                        1L,
                        "testimg.com",
                        "홍길동전",
                        5,
                        3L,
                        "A 포장",
                        1000,
                        50000);

        given(bookSaleService.getBookSaleDetail(anyLong()))
                .willReturn(List.of(bookSale));

        PaymentResponseDto payment = new PaymentResponseDto(
                1L,
                1L,
                "1232321",
                PaymentStatus.DONE,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "test.com");

        given(paymentService.getPayment(anyLong()))
                .willReturn(payment);


        SaleDetailResponseDto actual = saleFacade.getMemberSaleDetailBySaleNumber("1234", 1L);

        assertAll(
                () -> assertEquals(sale, actual.getSaleResponseDto()),
                () -> assertEquals(bookSale, actual.getBookSaleList().get(0)),
                () -> assertEquals(payment, actual.getPaymentResponseDto())
        );
    }

    @Test
    @DisplayName("주문 배송 상태 변경 테스트")
    void testUpdateSaleDeliveryStatus() {
        SaleDeliveryUpdateRequestDto delivery = new SaleDeliveryUpdateRequestDto();
        ReflectionTestUtils.setField(delivery, "deliveryStatus", DeliveryStatus.IN_PROGRESS);

        saleFacade.updateSaleDeliveryStatus(1L, delivery);
        verify(saleService, times(1)).updateSaleDeliveryStatus(anyLong(), any());
    }

    @Test
    @DisplayName("주문 취소 테스트 - 비회원")
    void testCancelSale_Anonymous() {
        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        null,
                        "테스트 제목",
                        null,
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        0,
                        15000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleDetail(1L))
                .willReturn(sale);

        saleFacade.cancelSale(1L);

        verify(saleService, times(1)).cancelSale(anyLong());
        verify(saleService, times(1)).getSaleDetail(anyLong());
        verify(memberService, times(0)).updateCancelSalePoint(anyLong(), anyString());
    }

    @Test
    @DisplayName("주문 취소 테스트 - 포인트를 사용한 회원")
    void testCancelSale_Member_UsePoint() {
        SaleResponseDto sale =
                new SaleResponseDto(
                        1L,
                        1L,
                        "테스트 제목",
                        "test@test.com",
                        "1234",
                        "정승조",
                        "01012345678",
                        "정승조",
                        "01012345678",
                        "광주광역시 동구 조선대 5길",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        LocalDate.now().plusDays(3),
                        DeliveryStatus.READY,
                        3000,
                        3000,
                        15000,
                        SalePaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleDetail(anyLong()))
                .willReturn(sale);

        saleFacade.cancelSale(1L);

        verify(saleService, times(1)).cancelSale(anyLong());
        verify(saleService, times(1)).getSaleDetail(anyLong());
        verify(memberService, times(1)).updateCancelSalePoint(anyLong(), anyString());
    }

    @Test
    @DisplayName("주문 확인 테스트 (도서 ID와 회원 ID를 통해 조회)")
    void testCheckSaleByMemberIdAndBookId() {

        SaleCheckResponseDto responseDto = new SaleCheckResponseDto(true);

        given(saleService.checkSaleByMemberIdAndBookId(anyLong(), anyLong()))
                .willReturn(responseDto);

        SaleCheckResponseDto actual = saleFacade.checkSaleByMemberIdAndBookId(1L, 1L);

        assertTrue(actual.getIsExist());
        verify(saleService, times(1)).checkSaleByMemberIdAndBookId(1L, 1L);
    }
}