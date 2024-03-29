package store.ckin.api.payment.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.entity.PaymentStatus;
import store.ckin.api.payment.repository.PaymentRepository;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

/**
 * 결제 서비스 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    SaleRepository saleRepository;

    @Test
    @DisplayName("결제 생성 테스트 - 실패(saleId가 존재하지 않을 경우)")
    void testCreatePayment() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        ReflectionTestUtils.setField(paymentRequestDto, "paymentKey", "12341234");
        ReflectionTestUtils.setField(paymentRequestDto, "saleNumber", "423421432");
        ReflectionTestUtils.setField(paymentRequestDto, "paymentStatus", PaymentStatus.DONE);
        ReflectionTestUtils.setField(paymentRequestDto, "requestedAt", LocalDateTime.now().minusMinutes(10));
        ReflectionTestUtils.setField(paymentRequestDto, "approvedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(paymentRequestDto, "amount", 15000);
        ReflectionTestUtils.setField(paymentRequestDto, "receiptUrl", "https://test.com");

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        Assertions.assertThrows(
                SaleNotFoundException.class,
                () -> paymentService.createPayment(1L, paymentRequestDto));

        verify(saleRepository, times(1)).findById(anyLong());
        verify(paymentRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("결제 생성 테스트 - 성공")
    void testCreatePaymentSuccess() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        ReflectionTestUtils.setField(paymentRequestDto, "paymentKey", "12341234");
        ReflectionTestUtils.setField(paymentRequestDto, "saleNumber", "423421432");
        ReflectionTestUtils.setField(paymentRequestDto, "paymentStatus", PaymentStatus.DONE);
        ReflectionTestUtils.setField(paymentRequestDto, "requestedAt", LocalDateTime.now().minusMinutes(10));
        ReflectionTestUtils.setField(paymentRequestDto, "approvedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(paymentRequestDto, "amount", 15000);
        ReflectionTestUtils.setField(paymentRequestDto, "receiptUrl", "https://test.com");


        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(Sale.builder().build()));

        paymentService.createPayment(1L, paymentRequestDto);

        verify(saleRepository, times(1)).findById(anyLong());
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문 ID로 결제 조회 테스트 - 실패 (존재하지 않는 주문)")
    void testGetPayment_Fail_SaleNotFound() {
        given(saleRepository.existsById(anyLong()))
                .willReturn(false);

        Assertions.assertThrows(
                SaleNotFoundException.class,
                () -> paymentService.getPayment(1L));

        verify(saleRepository, times(1)).existsById(anyLong());
        verify(paymentRepository, times(0)).getPaymentBySaleId(anyLong());
    }

    @Test
    @DisplayName("주문 ID로 결제 조회 테스트 - 성공")
    void testGetPayment_Success() {
        given(saleRepository.existsById(anyLong()))
                .willReturn(true);

        paymentService.getPayment(1L);

        verify(saleRepository, times(1)).existsById(anyLong());
        verify(paymentRepository, times(1)).getPaymentBySaleId(anyLong());
    }
}