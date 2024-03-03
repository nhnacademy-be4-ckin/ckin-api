package store.ckin.api.sale.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
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

    @PostMapping
    public ResponseEntity<Long> createSale(@Valid @RequestBody SaleCreateRequestDto requestDto) {
        Long saleId = saleFacade.createSale(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleId);
    }
}
