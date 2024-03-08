package store.ckin.api.sale.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import store.ckin.api.booksale.dto.response.BookSaleResponseDto;

/**
 * 주문 상세 정보와 주문한 책 정보 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@Getter
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

    private String postcode;

    private String address;

    private Integer pointUsage;

    private Integer totalPrice;

    public SaleWithBookResponseDto(Long saleId, String saleNumber, String memberEmail, String saleOrderName,
                                   String saleOrderContact,
                                   String saleReceiverName, String saleReceiverContact, Integer deliveryFee,
                                   LocalDate saleDeliveryDate, String postcode, String address, Integer pointUsage,
                                   Integer totalPrice) {
        this.saleId = saleId;
        this.saleNumber = saleNumber;
        this.memberEmail = memberEmail;
        this.saleOrdererName = saleOrderName;
        this.saleOrdererContact = saleOrderContact;
        this.saleReceiverName = saleReceiverName;
        this.saleReceiverContact = saleReceiverContact;
        this.deliveryFee = deliveryFee;
        this.saleDeliveryDate = saleDeliveryDate;
        this.postcode = postcode;
        this.address = address;
        this.pointUsage = pointUsage;
        this.totalPrice = totalPrice;
    }

    public void addBookSale(BookSaleResponseDto bookSale) {
        this.bookSaleList.add(bookSale);
    }

    public void updateSaleTitle(String saleTitle) {

        if (bookSaleList.size() == 1) {
            this.saleTitle = saleTitle;
            return;
        }

        this.saleTitle = saleTitle + " 외 " + (bookSaleList.size() - 1) + "권";
    }
}
