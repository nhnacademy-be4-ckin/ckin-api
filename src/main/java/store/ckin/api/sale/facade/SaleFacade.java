package store.ckin.api.sale.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.service.SaleService;

/**
 * 주문 퍼사드 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Service
@RequiredArgsConstructor
public class SaleFacade {

    private final SaleService saleService;

    private final BookSaleService bookSaleService;

    private final MemberService memberService;


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

        memberService.updatePoint(requestDto.getMemberId(), requestDto.getPointUsage());

        return saleId;
    }

    /**
     * 모든 주문 목록을 조회하는 메서드입니다.
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
    public SaleResponseDto getSaleDetail(Long saleId) {
        return saleService.getSaleDetail(saleId);
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
}
