package store.ckin.api.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.dto.response.PaymentSuccessResponseDto;
import store.ckin.api.payment.facade.PaymentFacade;

/**
 * 결제 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /**
     * 결제 생성 메서드입니다.
     *
     * @param requestDto 결제 생성 요청 DTO
     * @return 200 (OK), 결제 성공 응답 DTO
     */
    @PostMapping
    public ResponseEntity<PaymentSuccessResponseDto> createPayment(@RequestBody PaymentRequestDto requestDto) {
        log.info("requestDto = {}", requestDto);
        return ResponseEntity.ok(paymentFacade.createPayment(requestDto));
    }
}
