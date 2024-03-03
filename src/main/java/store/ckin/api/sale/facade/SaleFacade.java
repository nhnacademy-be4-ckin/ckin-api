package store.ckin.api.sale.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.booksale.service.BookSaleService;
import store.ckin.api.sale.dto.request.SaleCreateNoBookRequestDto;
import store.ckin.api.sale.dto.request.SaleCreateRequestDto;
import store.ckin.api.sale.service.SaleService;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@Service
@RequiredArgsConstructor
public class SaleFacade {

    private final SaleService saleService;

    private final BookSaleService bookSaleService;

    @Transactional
    public Long createSale(SaleCreateRequestDto requestDto) {

        SaleCreateNoBookRequestDto saleInfo =
                requestDto.toCreateSaleWithoutBookRequestDto();

        Long saleId = saleService.createSale(saleInfo);
        bookSaleService.createBookSale(saleId, requestDto.getBookSaleList());

        return saleId;
    }

}
