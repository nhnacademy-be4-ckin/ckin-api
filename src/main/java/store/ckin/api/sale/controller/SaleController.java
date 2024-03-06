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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
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
    public ResponseEntity<SaleResponseDto> getSaleInformation(@PathVariable("saleId") Long saleId) {

        return ResponseEntity.ok(saleFacade.getSaleInformation(saleId));
    }
}
