package store.ckin.api.sale.facade;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.service.PointHistoryService;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.request.SaleDeliveryUpdateRequestDto;
import store.ckin.api.sale.dto.response.SaleDetailResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.exception.SaleMemberNotMatchException;
import store.ckin.api.sale.exception.SaleOrdererContactNotMatchException;
import store.ckin.api.sale.service.SaleService;

/**
 * 주문 퍼사드 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleFacade {

    private final SaleService saleService;

    private final BookSaleService bookSaleService;

    private final MemberService memberService;

    private final PaymentService paymentService;

    private final PointHistoryService pointHistoryService;

    /**
     * 주문을 생성하는 메서드입니다.
     *
     * @param requestDto 주문 생성 요청 DTO
     * @return 생성된 주문 ID
     */
    @Transactional
    public String createSale(SaleCreateRequestDto requestDto) {

        SaleResponseDto sale = saleService.createSale(requestDto.toCreateSaleWithoutBookRequestDto());
        bookSaleService.createBookSale(sale.getSaleId(), requestDto.getBookSaleList());

        if (requestDto.getMemberId() != null && requestDto.getPointUsage() > 0) {
            memberService.updatePoint(requestDto.getMemberId(), -requestDto.getPointUsage());

            PointHistoryCreateRequestDto pointHistoryCreateRequestDto = PointHistoryCreateRequestDto.builder()
                    .memberId(requestDto.getMemberId())
                    .pointHistoryPoint(-requestDto.getPointUsage())
                    .pointHistoryReason("주문 사용")
                    .pointHistoryTime(sale.getSaleDate().toLocalDate())
                    .build();

            pointHistoryService.createPointHistory(pointHistoryCreateRequestDto);
        }

        return sale.getSaleNumber();
    }

    /**
     * 페이징 처리된 주문 목록을 조회하는 메서드입니다.
     *
     * @return 주문 DTO 리스트
     */
    @Transactional(readOnly = true)
    public PagedResponse<List<SaleResponseDto>> getSales(Pageable pageable) {
        return saleService.getSales(pageable);
    }


    /**
     * 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 주문 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public SaleDetailResponseDto getSaleDetail(Long saleId) {

        List<BookAndBookSaleResponseDto> bookSale = bookSaleService.getBookSaleDetail(saleId);

        SaleResponseDto saleDetail = saleService.getSaleDetail(saleId);
        PaymentResponseDto payment = paymentService.getPayment(saleId);


        return new SaleDetailResponseDto(bookSale, saleDetail, payment);
    }

    /**
     * 주문 번호로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 상세 정보와 주문한 책 정보
     */
    @Transactional(readOnly = true)
    public SaleWithBookResponseDto getSaleWithBookResponseDto(String saleNumber) {
        return saleService.getSaleWithBook(saleNumber);
    }

    /**
     * 주문 번호로 결제할 주문의 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 정보
     */
    @Transactional(readOnly = true)
    public SaleInfoResponseDto getSalePaymentInfo(String saleNumber) {
        return saleService.getSalePaymentInfo(saleNumber);
    }

    /**
     * 비회원의 주문 번호로 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber     주문 번호 (UUID)
     * @param ordererContact 주문자 연락처
     * @return 주문 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public SaleDetailResponseDto getGuestSaleDetailBySaleNumber(String saleNumber, String ordererContact) {

        SaleResponseDto saleDetail = saleService.getSaleBySaleNumber(saleNumber);

        if (!ordererContact.equals(saleDetail.getSaleOrdererContact())) {
            throw new SaleOrdererContactNotMatchException(saleNumber, ordererContact);
        }

        List<BookAndBookSaleResponseDto> bookSale = bookSaleService.getBookSaleDetail(saleDetail.getSaleId());
        PaymentResponseDto payment = paymentService.getPayment(saleDetail.getSaleId());
        return new SaleDetailResponseDto(bookSale, saleDetail, payment);
    }

    /**
     * 회원 ID와 주문 번호를 통해 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호
     * @param memberId   회원 ID
     * @return 주문 상세 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public SaleDetailResponseDto getMemberSaleDetailBySaleNumber(String saleNumber, Long memberId) {

        SaleResponseDto saleDetail = saleService.getSaleBySaleNumber(saleNumber);

        if (!Objects.equals(memberId, saleDetail.getMemberId())) {
            throw new SaleMemberNotMatchException(saleNumber);
        }

        List<BookAndBookSaleResponseDto> bookSale = bookSaleService.getBookSaleDetail(saleDetail.getSaleId());
        PaymentResponseDto payment = paymentService.getPayment(saleDetail.getSaleId());

        return new SaleDetailResponseDto(bookSale, saleDetail, payment);
    }

    /**
     * 회원 ID를 통해 해당 회원의 모든 주문 내역을 조회하는 메서드입니다.
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 페이징 처리된 주문 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public PagedResponse<List<SaleInfoResponseDto>> getSalesByMemberId(Long memberId, Pageable pageable) {
        return saleService.getSalesByMemberId(memberId, pageable);
    }

    /**
     * 주문 배송 상태를 업데이트하는 메서드입니다.
     *
     * @param saleId         주문 ID
     * @param deliveryStatus 배송 상태
     */
    @Transactional
    public void updateSaleDeliveryStatus(Long saleId, SaleDeliveryUpdateRequestDto deliveryStatus) {
        saleService.updateSaleDeliveryStatus(saleId, deliveryStatus);
    }

    /**
     * 주문을 취소하는 메서드입니다.
     *
     * @param saleId 주문 ID
     */
    @Transactional
    public void cancelSale(Long saleId) {

        // 회원 포인트 변경 및 포인트 이력 생성
        SaleResponseDto saleDetail = saleService.getSaleDetail(saleId);
        if (Objects.nonNull(saleDetail.getMemberEmail()) && saleDetail.getSalePointUsage() > 0) {
            memberService.updateCancelSalePoint(saleId, saleDetail.getMemberEmail());
        }

        // 주문 및 결제 상태 변경
        saleService.cancelSale(saleId);
    }
}
