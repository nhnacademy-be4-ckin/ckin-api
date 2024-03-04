package store.ckin.api.sale.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.repository.SaleRepository;
import store.ckin.api.sale.service.SaleService;

/**
 * 주문 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;

    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     *
     * @param requestDto 주문 생성 요청 DTO
     * @return 생성된 주문 ID
     */
    @Override
    @Transactional
    public Long createSale(SaleCreateNoBookRequestDto requestDto) {

        Optional<Member> member = Optional.empty();
        if (Objects.nonNull(requestDto.getMemberId())) {
            member = memberRepository.findById(requestDto.getMemberId());
        }


        String saleNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        Sale sale = Sale.builder()
                .member(member.orElse(null))
                .saleNumber(saleNumber)
                .saleOrdererName(requestDto.getSaleOrderName())
                .saleOrdererContact(requestDto.getSaleOrderContact())
                .saleReceiverName(requestDto.getSaleReceiverName())
                .saleReceiverContact(requestDto.getSaleReceiverContact())
                .saleReceiverAddress(requestDto.getAddress() + " " + requestDto.getDetailAddress())
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now().plusDays(1))
                .saleDeliveryDate(requestDto.getSaleDeliveryDate())
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(requestDto.getDeliveryFee())
                .salePointUsage(requestDto.getPointUsage())
                .saleTotalPrice(requestDto.getTotalPrice())
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode(requestDto.getPostcode())
                .build();

        return saleRepository.save(sale).getSaleId();
    }

    /**
     * {@inheritDoc}
     *
     * @return 주문 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSales() {
        return saleRepository.findAllOrderByIdDesc();
    }
}
