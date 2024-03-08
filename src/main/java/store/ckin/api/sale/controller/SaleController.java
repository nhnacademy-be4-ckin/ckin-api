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
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
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
     * @return 주문 번호
     */
    @PostMapping
    public ResponseEntity<Long> createSale(@Valid @RequestBody SaleCreateRequestDto requestDto) {
        Long saleId = saleFacade.createSale(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleId);
    }

    /**
     * 모든 주문 목록을 조회하는 메서드입니다.
     *
     * @return 주문 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<PagedResponse<List<SaleResponseDto>>> getSales(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(saleFacade.getSales(pageable));
    }


    /**
     * 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 주문 상세 정보 DTO
     */
    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponseDto> getSaleDetail(@PathVariable("saleId") Long saleId) {

        return ResponseEntity.ok(saleFacade.getSaleDetail(saleId));
    }

    /**
     * 주문 결제 상태를 결제 완료(PAID)로 변경하는 메서드입니다.
     * 주문의 상태를 업데이트하고, 책 주문의 상태를 완료(COMPLETE)로 업데이트합니다.
     *
     * @param saleId 주문 ID
     * @return 200 (OK)
     */
    @PutMapping("/{saleId}")
    public ResponseEntity<Void> updateSalePaymentPaidStatus(@PathVariable("saleId") Long saleId) {
        saleFacade.updateSalePaymentPaidStatus(saleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 주문 ID로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 200 (OK), 주문 상세 정보와 주문한 책 정보
     */
    @GetMapping("/{saleId}/books")
    public ResponseEntity<SaleWithBookResponseDto> getSaleWithBooks(@PathVariable Long saleId) {
        SaleWithBookResponseDto responseDto = saleFacade.SaleWithBookResponseDto(saleId);
        return ResponseEntity.ok(responseDto);
    }
}
