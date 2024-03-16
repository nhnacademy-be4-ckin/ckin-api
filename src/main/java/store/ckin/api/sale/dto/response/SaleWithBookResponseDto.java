package store.ckin.api.sale.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import store.ckin.api.booksale.dto.response.BookSaleResponseDto;

/**
 * 주문 상세 정보와 주문한 책 정보 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@Getter
@Builder
@AllArgsConstructor
public class SaleWithBookResponseDto {


    private final List<BookSaleResponseDto> bookSaleList = new ArrayList<>();

    private String saleTitle;

    private Long saleId;

    private String saleNumber;

    private String memberEmail;

    private String saleOrdererName;

    private String saleOrdererContact;

    private String saleReceiverName;

    private String saleReceiverContact;

    private Integer deliveryFee;

    private LocalDate saleDeliveryDate;

    private LocalDateTime saleDate;

    private String postcode;

    private String address;

    private Integer pointUsage;

    private Integer totalPrice;

    public void addBookSale(BookSaleResponseDto bookSale) {
        this.bookSaleList.add(bookSale);
    }

    public SaleInfoResponseDto extractSaleInfoResponseDto() {
        return new SaleInfoResponseDto(saleTitle, saleNumber, memberEmail, saleOrdererName, saleOrdererContact,
                totalPrice, saleDate);
    }
}
