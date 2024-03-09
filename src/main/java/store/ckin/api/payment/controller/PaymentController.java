package store.ckin.api.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import store.ckin.api.payment.dto.request.PaymentRequestDto;
import store.ckin.api.payment.facade.PaymentFacade;

/**
 * 결제 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody PaymentRequestDto requestDto) {
        paymentFacade.createPayment(requestDto);

        return ResponseEntity.ok().build();
    }
}
