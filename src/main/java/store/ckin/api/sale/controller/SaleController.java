package store.ckin.api.sale.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.request.SaleDeliveryUpdateRequestDto;
import store.ckin.api.sale.dto.response.SaleDetailResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.facade.SaleFacade;

/**
 * 주문 컨트롤러 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleFacade saleFacade;

    /**
     * 주문을 등록하는 메서드입니다.
     *
     * @param requestDto 주문 등록 요청 DTO
     * @return 201(CREATED), 주문 번호(UUID)
     */
    @PostMapping
    public ResponseEntity<String> createSale(@Valid @RequestBody SaleCreateRequestDto requestDto) {
        String saleNumber = saleFacade.createSale(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saleNumber);
    }

    /**
     * 모든 주문 목록을 조회하는 메서드입니다.
     *
     * @return 200(OK), 주문 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<PagedResponse<List<SaleResponseDto>>> getSales(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(saleFacade.getSales(pageable));
    }


    /**
     * 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 200(OK), 주문 상세 정보 DTO
     */
    @GetMapping("/{saleId}")
    public ResponseEntity<SaleDetailResponseDto> getSaleDetail(@PathVariable("saleId") Long saleId) {

        return ResponseEntity.ok(saleFacade.getSaleDetail(saleId));
    }

    /**
     * 주문 번호로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 200 (OK), 주문 상세 정보와 주문한 책 정보
     */
    @GetMapping("/{saleNumber}/books")
    public ResponseEntity<SaleWithBookResponseDto> getSaleWithBooks(@PathVariable String saleNumber) {
        SaleWithBookResponseDto responseDto = saleFacade.getSaleWithBookResponseDto(saleNumber);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 주문 번호로 결제할 주문의 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 200 (OK), 주문 정보
     */
    @GetMapping("/{saleNumber}/paymentInfo")
    public ResponseEntity<SaleInfoResponseDto> getSalePaymentInfo(@PathVariable("saleNumber") String saleNumber) {
        return ResponseEntity.ok(saleFacade.getSalePaymentInfo(saleNumber));
    }

    /**
     * 비회원의 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호
     * @return 200 (OK), 주문 상세 정보
     */
    @GetMapping("/guest")
    public ResponseEntity<SaleDetailResponseDto> getGuestSaleDetailBySaleNumber(
            @RequestParam("saleNumber") String saleNumber,
            @RequestParam("ordererContact") String ordererContact) {
        return ResponseEntity.ok(saleFacade.getGuestSaleDetailBySaleNumber(saleNumber, ordererContact));
    }

    /**
     * 회원의 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호
     * @param memberId   회원 ID
     * @return 200 (OK), 주문 상세 정보
     */
    @GetMapping("/member")
    public ResponseEntity<SaleDetailResponseDto> getMemberSaleDetailBySaleNumber(
            @RequestParam("saleNumber") String saleNumber,
            @RequestParam("memberId") Long memberId) {

        return ResponseEntity.ok(saleFacade.getMemberSaleDetailBySaleNumber(saleNumber, memberId));
    }

    /**
     * 회원의 모든 주문을 조회하는 메서드입니다.
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 200 (OK), 회원의 주문 응답 DTO 리스트
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<PagedResponse<List<SaleInfoResponseDto>>> getSalesByMemberId(
            @PathVariable Long memberId,
            @PageableDefault Pageable pageable) {

        return ResponseEntity.ok(saleFacade.getSalesByMemberId(memberId, pageable));
    }

    /**
     * 주문 배송 상태를 업데이트하는 메서드입니다.
     *
     * @param saleId         주문 ID
     * @param deliveryStatus 배송 상태
     * @return 200 (OK)
     */
    @PutMapping("/{saleId}/delivery/status")
    public ResponseEntity<Void> updateDeliveryStatus(@PathVariable Long saleId,
                                                     @Valid @RequestBody
                                                     SaleDeliveryUpdateRequestDto deliveryStatus) {

        saleFacade.updateSaleDeliveryStatus(saleId, deliveryStatus);
        return ResponseEntity.ok().build();
    }

    /**
     * 주문 상태를 취소로 변경하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 200 (OK)
     */
    @PutMapping("/{saleId}/cancel")
    public ResponseEntity<Void> cancelSale(@PathVariable Long saleId) {
        saleFacade.cancelSale(saleId);
        return ResponseEntity.ok().build();
    }
}
