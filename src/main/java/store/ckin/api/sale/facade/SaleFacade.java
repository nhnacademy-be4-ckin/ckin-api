package store.ckin.api.sale.facade;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.domain.response.MemberInfoDetailResponseDto;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.pointhistory.dto.request.PointHistoryCreateRequestDto;
import store.ckin.api.pointhistory.service.PointHistoryService;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
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

        log.info("bookSale = {}", bookSale);

        SaleResponseDto saleDetail = saleService.getSaleDetail(saleId);
        PaymentResponseDto payment = paymentService.getPayment(saleId);


        return new SaleDetailResponseDto(bookSale, saleDetail, payment);
    }

    /**
     * 주문의 결제 상태를 결제 완료(PAID)로 업데이트하는 메서드입니다.
     * 주문의 상태를 업데이트하고, 책 주문의 상태를 완료(COMPLETE)로 업데이트합니다.
     *
     * @param saleId 주문 ID
     */
    @Transactional
    public void updateSalePaymentPaidStatus(Long saleId) {
        bookSaleService.updateBookSaleState(saleId, BookSale.BookSaleState.COMPLETE);
        saleService.updateSalePaymentPaidStatus(saleId);
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
     * 회원의 주문 번호를 통해 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호
     * @param memberId   회원 ID
     * @return 주문 상세 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public SaleDetailResponseDto getMemberSaleDetailBySaleNumber(String saleNumber, Long memberId) {

        SaleResponseDto saleDetail = saleService.getSaleBySaleNumber(saleNumber);
        MemberInfoDetailResponseDto memberInfo = memberService.getMemberInfoDetail(memberId);

        if (!Objects.equals(memberInfo.getEmail(), saleDetail.getMemberEmail())) {
            throw new SaleMemberNotMatchException(saleNumber);
        }

        List<BookAndBookSaleResponseDto> bookSale = bookSaleService.getBookSaleDetail(saleDetail.getSaleId());
        PaymentResponseDto payment = paymentService.getPayment(saleDetail.getSaleId());

        return new SaleDetailResponseDto(bookSale, saleDetail, payment);
    }

    /**
     * 주문 번호로 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber     주문 번호
     * @param memberId       회원 ID
     * @param ordererContact 주문자 연락처
     * @return 주문 상세 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public SaleDetailResponseDto getSaleDetailBySaleNumber(String saleNumber, Long memberId, String ordererContact) {
        SaleResponseDto saleDetail = saleService.getSaleBySaleNumber(saleNumber);

        if (memberId != null) { // 회원인 경우
            if (!Objects.equals(memberId, saleDetail.getSaleId())) {
                throw new SaleMemberNotMatchException(saleNumber);
            }
        } else { // 비회원인 경우
            if (!ordererContact.equals(saleDetail.getSaleOrdererContact())) {
                throw new SaleOrdererContactNotMatchException(saleNumber, ordererContact);
            }
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
     * 회원 등급의 적립률에 따라 적립 포인트를 업데이트하는 메서드입니다.
     *
     * @param memberId   회원 ID
     * @param totalPrice 주문 금액
     */
    @Transactional
    public void createRewardPointHistory(Long memberId, Integer totalPrice) {
        memberService.updateRewardPoint(memberId, totalPrice);
    }
}
