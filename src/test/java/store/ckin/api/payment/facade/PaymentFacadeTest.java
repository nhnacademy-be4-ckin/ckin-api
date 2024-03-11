package store.ckin.api.payment.facade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.exception.PaymentAmountNotCorrectException;
import store.ckin.api.payment.exception.PaymentNotCompleteException;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.service.SaleService;

/**
 * 결제 퍼사드 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */

@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {

    @InjectMocks
    PaymentFacade paymentFacade;

    @Mock
    PaymentService paymentService;

    @Mock
    SaleService saleService;

    PaymentRequestDto failPayment;

    PaymentRequestDto successPayment;


    @BeforeEach
    void setUp() {
        failPayment = new PaymentRequestDto(
                "12341234",
                "423421432",
                "FAIL",
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now(),
                15000,
                "https://test.com"
        );

        successPayment = new PaymentRequestDto(
                "12341234",
                "423421432",
                "DONE",
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now(),
                15000,
                "https://test.com"
        );


    }

    @Test
    @DisplayName("결제 생성 테스트 - 실패 (응답이 'DONE' 이 아닌 경우)")
    void testCreatePayment_NotCompleteException() {

        Assertions.assertThrows(
                PaymentNotCompleteException.class,
                () -> paymentFacade.createPayment(failPayment)
        );

        verify(saleService, times(0)).getSaleDetailBySaleNumber(anyString());
        verify(paymentService, times(0)).createPayment(anyLong(), any());
    }

    @Test
    @DisplayName("결제 생성 테스트 - 실패 (결제 금액이 일치하지 않는 경우)")
    void testCreatePayment_AmountNotCorrectException() {

        SaleResponseDto saleResponseDto =
                new SaleResponseDto(
                        1L,
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
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        10000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleDetailBySaleNumber(anyString()))
                .willReturn(saleResponseDto);

        Assertions.assertThrows(
                PaymentAmountNotCorrectException.class,
                () -> paymentFacade.createPayment(successPayment)
        );

        verify(saleService, times(1)).getSaleDetailBySaleNumber(anyString());
        verify(paymentService, times(0)).createPayment(anyLong(), any());
    }

    @Test
    @DisplayName("결제 생성 테스트 - 성공")
    void testCreatePayment() {

        SaleResponseDto saleResponseDto =
                new SaleResponseDto(
                        1L,
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
                        Sale.DeliveryStatus.READY,
                        3000,
                        0,
                        15000,
                        Sale.PaymentStatus.WAITING,
                        "123456"
                );

        given(saleService.getSaleDetailBySaleNumber(anyString()))
                .willReturn(saleResponseDto);

        paymentFacade.createPayment(successPayment);

        verify(saleService, times(1)).getSaleDetailBySaleNumber(anyString());
        verify(paymentService, times(1)).createPayment(anyLong(), any());
    }
}