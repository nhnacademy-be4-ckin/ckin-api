package store.ckin.api.sale.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.exception.SaleNotFoundException;
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
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .saleTotalPrice(10000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void testCreateSale() {
        // given
        SaleCreateNoBookRequestDto requestDto = new SaleCreateNoBookRequestDto(
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

        Optional<Member> optionalMember = Optional.of(member);
        given(memberRepository.findById(anyLong()))
                .willReturn(optionalMember);


        given(saleRepository.save(any()))
                .willReturn(sale);

        // when
        Long saleId = saleService.createSale(requestDto);

        // then
        assertNotNull(saleId);
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
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .saleTotalPrice(10000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
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

        assertEquals(Sale.PaymentStatus.PAID, sale.getSalePaymentStatus());
    }
}