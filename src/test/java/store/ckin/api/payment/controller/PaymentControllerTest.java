package store.ckin.api.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.dto.response.PaymentSuccessResponseDto;
import store.ckin.api.payment.facade.PaymentFacade;

/**
 * 결제 컨트롤러 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */

@Slf4j
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentFacade paymentFacade;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("결제 생성 테스트")
    void testCreatePayment() throws Exception {

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        ReflectionTestUtils.setField(paymentRequestDto, "paymentKey", "12341234");
        ReflectionTestUtils.setField(paymentRequestDto, "saleNumber", "423421432");
        ReflectionTestUtils.setField(paymentRequestDto, "paymentStatus", "DONE");
        ReflectionTestUtils.setField(paymentRequestDto, "requestedAt", LocalDateTime.now().minusMinutes(10));
        ReflectionTestUtils.setField(paymentRequestDto, "approvedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(paymentRequestDto, "amount", 15000);
        ReflectionTestUtils.setField(paymentRequestDto, "receiptUrl", "https://test.com");

        String json = objectMapper.writeValueAsString(paymentRequestDto);

        PaymentSuccessResponseDto successResponseDto = PaymentSuccessResponseDto.builder()
                .saleNumber("423421432")
                .receiverName("정승조")
                .receiverContact("01012341234")
                .address("광주광역시 동구")
                .deliveryDate(LocalDate.now().plusDays(3))
                .saleTotalPrice(15000)
                .receiptUrl("https://test.com")
                .build();

        given(paymentFacade.createPayment(any()))
                .willReturn(successResponseDto);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.saleNumber").value(successResponseDto.getSaleNumber()),
                        jsonPath("$.receiverName").value(successResponseDto.getReceiverName()),
                        jsonPath("$.receiverContact").value(successResponseDto.getReceiverContact()),
                        jsonPath("$.address").value(successResponseDto.getAddress()),
                        jsonPath("$.deliveryDate").value(successResponseDto.getDeliveryDate().toString()),
                        jsonPath("$.saleTotalPrice").value(successResponseDto.getSaleTotalPrice()),
                        jsonPath("$.receiptUrl").value(successResponseDto.getReceiptUrl()))
                .andDo(print());

        verify(paymentFacade, times(1)).createPayment(any());
    }

}