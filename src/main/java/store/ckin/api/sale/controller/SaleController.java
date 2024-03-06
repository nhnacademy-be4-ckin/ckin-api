package store.ckin.api.sale.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.facade.SaleFacade;

/**
 * 주문 컨트롤러 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Slf4j
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
    public ResponseEntity<List<SaleResponseDto>> getSales() {
        return ResponseEntity.ok(saleFacade.getSales());
    }


    /**
     * 주문 상세 정보를 조회하는 메서드입니다.
     *
     * @param saleId 주문 ID
     * @return 주문 상세 정보 DTO
     */
    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponseDto> getSaleInformation(@PathVariable("saleId") Long saleId) {

        log.debug("saleId = {}", saleId);
        return ResponseEntity.ok(saleFacade.getSaleInformation(saleId));
    }
}
