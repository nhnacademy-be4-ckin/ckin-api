package store.ckin.api.payment.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.payment.dto.response.PaymentResponseDto;

/**
 * 결제 레포지토리 커스텀 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */

@NoRepositoryBean
public interface PaymentRepositoryCustom {

    PaymentResponseDto getPaymentBySaleId(Long saleId);
}
