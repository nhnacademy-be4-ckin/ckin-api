package store.ckin.api.sale.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.exception.SaleNotFoundExceptionBySaleNumber;
import store.ckin.api.sale.exception.SaleNumberNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;
import store.ckin.api.sale.service.SaleService;

/**
 * 주문 서비스 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Slf4j
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
    public SaleResponseDto createSale(SaleCreateNoBookRequestDto requestDto) {

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


        Sale savedSale = saleRepository.save(sale);
        return SaleResponseDto.toDto(savedSale);
    }

    /**
     * {@inheritDoc}
     *
     * @return 주문 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<List<SaleResponseDto>> getSales(Pageable pageable) {
        Page<Sale> salePage = saleRepository.findAllByOrderBySaleIdDesc(pageable);

        PageInfo pageInfo = PageInfo.builder().page(pageable.getPageNumber()).size(pageable.getPageSize())
                .totalElements((int) salePage.getTotalElements()).totalPages(salePage.getTotalPages()).build();

        List<SaleResponseDto> currentPageSalesResponse =
                salePage.getContent().stream().map(SaleResponseDto::toDto).collect(Collectors.toList());

        return new PagedResponse<>(currentPageSalesResponse, pageInfo);
    }


    /**
     * {@inheritDoc}
     *
     * @param saleId
     * @return 주문 조회 응답 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleDetail(Long saleId) {

        if (!saleRepository.existsById(saleId)) {
            throw new SaleNotFoundException(saleId);
        }

        return saleRepository.findBySaleId(saleId);
    }

    /**
     * {@inheritDoc}
     *
     * @param saleId 주문 ID
     */
    @Override
    @Transactional
    public void updateSalePaymentPaidStatus(Long saleId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new SaleNotFoundException(saleId));

        sale.updatePaymentStatus(Sale.PaymentStatus.PAID);
    }


    /**
     * {@inheritDoc}
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 상세 정보와 주문한 책 정보 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public SaleWithBookResponseDto getSaleWithBook(String saleNumber) {

        if (!saleRepository.existsBySaleNumber(saleNumber)) {
            throw new SaleNotFoundExceptionBySaleNumber(saleNumber);
        }

        return saleRepository.getSaleWithBook(saleNumber);
    }

    /**
     * {@inheritDoc}
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 결제 정보 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public SaleInfoResponseDto getSalePaymentInfo(String saleNumber) {

        if (!saleRepository.existsBySaleNumber(saleNumber)) {
            throw new SaleNumberNotFoundException(saleNumber);
        }


        return saleRepository.getSaleWithBook(saleNumber).extractSaleInfoResponseDto();
    }

    /**
     * {@inheritDoc}
     *
     * @param saleNumber
     * @return 주문 조회 응답 DTO
     */
    @Override
    public SaleResponseDto getSaleBySaleNumber(String saleNumber) {

        if (!saleRepository.existsBySaleNumber(saleNumber)) {
            throw new SaleNumberNotFoundException(saleNumber);
        }

        SaleResponseDto responseDto = saleRepository.findBySaleNumber(saleNumber);

        log.debug("responseDto = {}", responseDto);
        return responseDto;
    }
}
