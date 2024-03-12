package store.ckin.api.sale.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.service.PaymentService;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.response.SaleDetailResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
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

    /**
     * 주문을 생성하는 메서드입니다.
     *
     * @param requestDto 주문 생성 요청 DTO
     * @return 생성된 주문 ID
     */
    @Transactional
    public Long createSale(SaleCreateRequestDto requestDto) {

        SaleCreateNoBookRequestDto saleInfo =
                requestDto.toCreateSaleWithoutBookRequestDto();

        Long saleId = saleService.createSale(saleInfo);

        bookSaleService.createBookSale(saleId, requestDto.getBookSaleList());

        if (requestDto.getMemberId() != null && requestDto.getPointUsage() > 0) {
            memberService.updatePoint(requestDto.getMemberId(), requestDto.getPointUsage());
        }

        return saleId;
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
     * 주문 ID로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 주문 상세 정보와 주문한 책 정보
     */
    @Transactional(readOnly = true)
    public SaleWithBookResponseDto getSaleWithBookResponseDto(Long saleId) {
        return saleService.getSaleWithBook(saleId);
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
}
