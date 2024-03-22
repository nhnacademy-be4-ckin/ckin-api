package store.ckin.api.sale.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.payment.repository.PaymentRepository;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.request.SaleDeliveryUpdateRequestDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.DeliveryStatus;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.entity.SalePaymentStatus;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.exception.SaleNotFoundExceptionBySaleNumber;
import store.ckin.api.sale.exception.SaleNumberNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

/**
 * 주문 서비스 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @InjectMocks
    SaleServiceImpl saleService;

    @Mock
    SaleRepository saleRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PaymentRepository paymentRepository;

    Grade grade;

    Member member;

    Sale sale;

    @BeforeEach
    void setUp() {

        grade = Grade.builder()
                .name("GOLD")
                .pointRatio(10)
                .build();

        member = Member.builder()
                .grade(grade)
                .email("seungjo@nhn.com")
                .password("1234")
                .name("SEUNGJO")
                .contact("01012341234")
                .birth(LocalDate.of(1999, 5, 13))
                .state(Member.State.ACTIVE)
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .accumulateAmount(10000)
                .build();

        sale = Sale.builder()
                .saleId(1L)
                .member(member)
                .saleNumber("12345678901234567890")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012345678")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012345678")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .saleTotalPrice(10000)
                .salePaymentStatus(SalePaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void testCreateSale() {
        // given
        SaleCreateNoBookRequestDto requestDto = new SaleCreateNoBookRequestDto(
                1L,
                "홍길동전",
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

        Optional<Member> optionalMember = Optional.of(member);
        given(memberRepository.findById(anyLong()))
                .willReturn(optionalMember);


        given(saleRepository.save(any()))
                .willReturn(sale);

        SaleResponseDto saleDto = saleService.createSale(requestDto);

        // then
        assertNotNull(saleDto);
        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 페이징 조회 테스트")
    void testGetSales() {


        Sale testSale = Sale.builder()
                .saleId(2L)
                .member(member)
                .saleNumber("12345678901234567890")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012345678")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012345678")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .saleTotalPrice(10000)
                .salePaymentStatus(SalePaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        Page<Sale> salePage = new PageImpl<>(List.of(sale, testSale));

        given(saleRepository.findAllByOrderBySaleIdDesc(any()))
                .willReturn(salePage);

        PagedResponse<List<SaleResponseDto>> sales = saleService.getSales(Pageable.ofSize(10));

        assertEquals(2, sales.getData().size());
        assertEquals(1, sales.getPageInfo().getTotalPages());
    }

    @Test
    @DisplayName("주문 ID로 주문 조회 테스트 - 실패")
    void testGetSaleDetail_Fail() {
        given(saleRepository.existsById(anyLong()))
                .willReturn(false);

        assertThrows(SaleNotFoundException.class, () -> saleService.getSaleDetail(1L));

        verify(saleRepository, times(1)).existsById(anyLong());
        verify(saleRepository, times(0)).findBySaleId(anyLong());
    }

    @Test
    @DisplayName("주문 ID로 주문 조회 테스트 - 성공")
    void testGetSaleDetail_Success() {
        given(saleRepository.existsById(anyLong()))
                .willReturn(true);

        SaleResponseDto responseDto = SaleResponseDto.toDto(sale);

        given(saleRepository.findBySaleId(anyLong()))
                .willReturn(responseDto);

        SaleResponseDto saleDetail = saleService.getSaleDetail(1L);

        assertAll(
                () -> assertEquals(saleDetail.getSaleId(), sale.getSaleId()),
                () -> assertEquals(saleDetail.getSaleNumber(), sale.getSaleNumber())
        );

        verify(saleRepository, times(1)).existsById(anyLong());
        verify(saleRepository, times(1)).findBySaleId(anyLong());
    }

    @Test
    @DisplayName("주문 결제 상태를 완료로 변경 테스트 - 실패")
    void testUpdateSalePaymentPaidStatus_Fail() {

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class, () -> saleService.updateSalePaymentPaidStatus(1L));
    }

    @Test
    @DisplayName("주문 결제 상태를 완료로 변경 테스트 - 성공")
    void testUpdateSalePaymentPaidStatus_Success() {

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        saleService.updateSalePaymentPaidStatus(1L);

        assertEquals(SalePaymentStatus.PAID, sale.getSalePaymentStatus());
    }

    @Test
    @DisplayName("주문 ID로 주문 상세 정보와 주문한 책 정보 조회 테스트 - 실패")
    void testGetSaleWithBook_Fail() {

        given(saleRepository.existsBySaleNumber(anyString()))
                .willReturn(false);

        assertThrows(SaleNotFoundExceptionBySaleNumber.class,
                () -> saleService.getSaleWithBook("123vas"));

        verify(saleRepository, times(1)).existsBySaleNumber(anyString());
        verify(saleRepository, times(0)).getSaleWithBook(anyString());
    }

    @Test
    @DisplayName("주문 ID로 주문 상세 정보와 주문한 책 정보 조회 테스트 - 성공")
    void testGetSaleWithBook_Success() {

        given(saleRepository.existsBySaleNumber(anyString()))
                .willReturn(true);

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

        given(saleRepository.getSaleWithBook(anyString()))
                .willReturn(responseDto);

        SaleWithBookResponseDto saleWithBook = saleService.getSaleWithBook("123abc");

        assertAll(
                () -> assertEquals(saleWithBook.getSaleId(), responseDto.getSaleId()),
                () -> assertEquals(saleWithBook.getSaleNumber(), responseDto.getSaleNumber()),
                () -> assertEquals(saleWithBook.getSaleOrdererName(), responseDto.getSaleOrdererName()),
                () -> assertEquals(saleWithBook.getSaleOrdererContact(), responseDto.getSaleOrdererContact()),
                () -> assertEquals(saleWithBook.getSaleReceiverName(), responseDto.getSaleReceiverName()),
                () -> assertEquals(saleWithBook.getSaleReceiverContact(), responseDto.getSaleReceiverContact()),
                () -> assertEquals(saleWithBook.getDeliveryFee(), responseDto.getDeliveryFee()),
                () -> assertEquals(saleWithBook.getSaleDeliveryDate(), responseDto.getSaleDeliveryDate()),
                () -> assertEquals(saleWithBook.getPostcode(), responseDto.getPostcode()),
                () -> assertEquals(saleWithBook.getAddress(), responseDto.getAddress()),
                () -> assertEquals(saleWithBook.getPointUsage(), responseDto.getPointUsage()),
                () -> assertEquals(saleWithBook.getTotalPrice(), responseDto.getTotalPrice())
        );

        verify(saleRepository, times(1)).existsBySaleNumber(anyString());
        verify(saleRepository, times(1)).getSaleWithBook(anyString());
    }

    @Test
    @DisplayName("결제할 주문 정보 조회 테스트 - 실패")
    void testGetSalePaymentInfo_Fail() {

        given(saleRepository.existsBySaleNumber(anyString()))
                .willReturn(false);

        assertThrows(SaleNumberNotFoundException.class, () -> saleService.getSalePaymentInfo("ABC213"));

        verify(saleRepository, times(1)).existsBySaleNumber(anyString());
        verify(saleRepository, times(0)).getBySaleNumber(anyString());
        verify(saleRepository, times(0)).getSaleWithBook(anyString());
    }

    @Test
    @DisplayName("결제할 주문 정보 조회 테스트 - 성공")
    void testGetSalePaymentInfo_Success() {

        given(saleRepository.existsBySaleNumber(anyString()))
                .willReturn(true);

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
                LocalDate.now().plusDays(1),
                LocalDateTime.now(),
                "12345",
                "광주광역시 동구 조선대 5길",
                0,
                10000
        );

        given(saleRepository.getSaleWithBook(anyString()))
                .willReturn(responseDto);

        SaleInfoResponseDto saleInfo = saleService.getSalePaymentInfo("ABC1234DEF");

        assertAll(
                () -> assertEquals(saleInfo.getSaleTitle(), responseDto.getSaleTitle()),
                () -> assertEquals(saleInfo.getSaleNumber(), responseDto.getSaleNumber()),
                () -> assertEquals(saleInfo.getSaleOrdererName(), responseDto.getSaleOrdererName()),
                () -> assertEquals(saleInfo.getSaleOrdererContact(), responseDto.getSaleOrdererContact()),
                () -> assertEquals(saleInfo.getTotalPrice(), responseDto.getTotalPrice()
                )
        );

        verify(saleRepository, times(1)).existsBySaleNumber(anyString());
        verify(saleRepository, times(1)).getSaleWithBook(anyString());
    }

    @Test
    @DisplayName("주문 번호로 주문 조회 테스트 - 실패(존재하지 않는 주문 번호)")
    void testGetSaleDetailBySaleNumber_Fail() {

        given(saleRepository.existsBySaleNumber(anyString()))
                .willReturn(false);

        assertThrows(SaleNumberNotFoundException.class, () -> saleService.getSaleBySaleNumber("123456"));

        verify(saleRepository, times(1)).existsBySaleNumber(anyString());
        verify(saleRepository, times(0)).findBySaleNumber(anyString());
    }

    @Test
    @DisplayName("주분 번호로 주문 조회 테스트 - 성공")
    void testGetSaleDetailBySaleNumber_Success() {

        given(saleRepository.existsBySaleNumber(anyString()))
                .willReturn(true);

        given(saleRepository.findBySaleNumber(anyString()))
                .willReturn(SaleResponseDto.toDto(sale));

        SaleResponseDto saleDetail = saleService.getSaleBySaleNumber("12345213");

        assertAll(
                () -> assertEquals(saleDetail.getSaleId(), sale.getSaleId()),
                () -> assertEquals(saleDetail.getSaleNumber(), sale.getSaleNumber()),
                () -> assertEquals(saleDetail.getSaleOrdererName(), sale.getSaleOrdererName()),
                () -> assertEquals(saleDetail.getSaleOrdererContact(), sale.getSaleOrdererContact()),
                () -> assertEquals(saleDetail.getSaleReceiverName(), sale.getSaleReceiverName()),
                () -> assertEquals(saleDetail.getSaleReceiverContact(), sale.getSaleReceiverContact()),
                () -> assertEquals(saleDetail.getSaleReceiverAddress(), sale.getSaleReceiverAddress()),
                () -> assertNotNull(saleDetail.getSaleDeliveryDate()),
                () -> assertEquals(saleDetail.getSaleDeliveryStatus(), sale.getSaleDeliveryStatus()),
                () -> assertEquals(saleDetail.getSaleDeliveryFee(), sale.getSaleDeliveryFee()),
                () -> assertEquals(saleDetail.getSalePointUsage(), sale.getSalePointUsage()),
                () -> assertEquals(saleDetail.getSaleTotalPrice(), sale.getSaleTotalPrice()),
                () -> assertEquals(saleDetail.getSalePaymentStatus(), sale.getSalePaymentStatus()),
                () -> assertEquals(saleDetail.getSaleShippingPostCode(), sale.getSaleShippingPostCode())
        );

        verify(saleRepository, times(1)).existsBySaleNumber(anyString());
    }

    @Test
    @DisplayName("회원 ID로 주문 조회 테스트")
    void testGetSalesByMemberId() {

        SaleInfoResponseDto saleInfo
                = new SaleInfoResponseDto("홍길동전 외 3권",
                "123aqbc4",
                "test@test.com",
                "Tester",
                "010123211234",
                45000,
                LocalDateTime.of(2024, 3, 7, 12, 0));


        PageInfo pageInfo = new PageInfo(1, 10, 45, 5);
        PagedResponse<List<SaleInfoResponseDto>> sales = new PagedResponse<>(List.of(saleInfo), pageInfo);

        given(saleRepository.findAllByMemberId(anyLong(), any()))
                .willReturn(sales);

        PagedResponse<List<SaleInfoResponseDto>> responseDto =
                saleService.getSalesByMemberId(1L, Pageable.ofSize(10));


        assertAll(
                () -> assertNotNull(responseDto),
                () -> assertEquals(responseDto.getData().get(0), saleInfo),
                () -> assertEquals(sales.getPageInfo(), pageInfo)
        );

        verify(saleRepository, times(1)).findAllByMemberId(anyLong(), any());
    }

    @Test
    @DisplayName("주문 배송 상태 업데이트 - 실패")
    void testUpdateSaleDeliveryStatus_Fail() {
        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        SaleDeliveryUpdateRequestDto delivery = new SaleDeliveryUpdateRequestDto();
        ReflectionTestUtils.setField(delivery, "deliveryStatus", DeliveryStatus.IN_PROGRESS);

        assertThrows(SaleNotFoundException.class,
                () -> saleService.updateSaleDeliveryStatus(1L, delivery));
    }

    @Test
    @DisplayName("주문 배송 상태 업데이트 - 성공")
    void testUpdateSaleDeliveryStatus_Success() {
        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        SaleDeliveryUpdateRequestDto delivery = new SaleDeliveryUpdateRequestDto();
        ReflectionTestUtils.setField(delivery, "deliveryStatus", DeliveryStatus.IN_PROGRESS);

        saleService.updateSaleDeliveryStatus(1L, delivery);

        assertEquals(DeliveryStatus.IN_PROGRESS, sale.getSaleDeliveryStatus());
    }

    @Test
    @DisplayName("주문 취소 테스트 - 실패 (존재하지 않는 주문)")
    void testCancelSale_Fail() {
        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class, () -> saleService.cancelSale(1L));

        verify(saleRepository, times(1)).findById(anyLong());
        verify(paymentRepository, times(0)).findBySale_SaleId(anyLong());
    }

    @Test
    @DisplayName("주문 취소 테스트 - 성공")
    void testCancelSale_Success() {
        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        saleService.cancelSale(1L);
        assertEquals(SalePaymentStatus.CANCEL, sale.getSalePaymentStatus());

        verify(saleRepository, times(1)).findById(anyLong());
        verify(paymentRepository, times(1)).findBySale_SaleId(anyLong());
    }
}