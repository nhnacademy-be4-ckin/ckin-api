package store.ckin.api.sale.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.request.SaleDeliveryUpdateRequestDto;
import store.ckin.api.sale.dto.response.SaleCheckResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;

/**
 * 주문 서비스 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */
public interface SaleService {

    /**
     * 주문 생성 요청을 합니다.
     *
     * @param requestDto 주문 생성 요청 DTO
     * @return 생성된 주문 ID
     */
    SaleResponseDto createSale(SaleCreateNoBookRequestDto requestDto);


    /**
     * 페이징 처리된 주문 목록을 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 주문 응답 DTO 리스트
     */
    PagedResponse<List<SaleResponseDto>> getSales(Pageable pageable);

    /**
     * 주문 상세 정보를 조회합니다.
     *
     * @param saleId 주문 ID
     * @return 주문 응답 DTO
     */
    SaleResponseDto getSaleDetail(Long saleId);


    /**
     * 주문 결제 상태를 결제 완료(PAID))로 변경합니다.
     *
     * @param saleId 주문 ID
     */
    void updateSalePaymentPaidStatus(Long saleId);


    /**
     * 주문 ID로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 상세 정보와 주문한 책 정보 DTO
     */
    SaleWithBookResponseDto getSaleWithBook(String saleNumber);

    /**
     * 주문 결제 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 결제 정보 DTO
     */
    SaleInfoResponseDto getSalePaymentInfo(String saleNumber);

    /**
     * 주문 번호로 주문을 조회합니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 정보 DTO
     */
    SaleResponseDto getSaleBySaleNumber(String saleNumber);

    /**
     * 회원 ID를 통해 해당 회원의 모든 주문 내역을 조회합니다.
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 페이징 처리된 주문 응답 DTO 리스트
     */
    PagedResponse<List<SaleInfoResponseDto>> getSalesByMemberId(Long memberId, Pageable pageable);

    /**
     * 주문 배송 상태를 업데이트합니다.
     *
     * @param saleId         주문 ID
     * @param deliveryStatus 배송 상태
     */
    void updateSaleDeliveryStatus(Long saleId, SaleDeliveryUpdateRequestDto deliveryStatus);

    /**
     * 주문을 취소 상태로 변경합니다.
     *
     * @param saleId 주문 ID
     */
    void cancelSale(Long saleId);

    /**
     * 회원 ID와 도서 ID를 통해 주문 리스트에 해당 주문이 존재하는지 확인하는 메서드입니다.
     *
     * @param memberId 회원 ID
     * @param bookId   도서 ID
     * @return 주문 확인 응답 DTO
     */
    SaleCheckResponseDto checkSaleByMemberIdAndBookId(Long memberId, Long bookId);
}
