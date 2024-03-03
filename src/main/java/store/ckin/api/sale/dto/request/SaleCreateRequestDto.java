package store.ckin.api.sale.dto.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import store.ckin.api.booksale.dto.request.BookSaleCreateRequestDto;

/**
 * 주문 생성 요청 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

@ToString
@Getter
@AllArgsConstructor
public class SaleCreateRequestDto {

    private final List<BookSaleCreateRequestDto> bookSaleList = new ArrayList<>();

    private Long memberId;

    @NotBlank(message = "주문자 이름을 입력해주세요.")
    private String saleOrderName;

    @NotBlank(message = "주문자 연락처를 입력해주세요.")
    private String saleOrderContact;

    @NotBlank(message = "수령자 이름을 입력해주세요.")
    private String saleReceiverName;

    @NotBlank(message = "수령자 연락처를 입력해주세요.")
    private String saleReceiverContact;

    @NotNull(message = "배송비를 입력해주세요.")
    private Integer deliveryFee;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "배송 날짜를 선택해주세요.")
    private LocalDate saleDeliveryDate;

    @NotNull(message = "우편번호를 입력해주세요.")
    private Integer postcode;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    private String detailAddress;

    @PositiveOrZero(message = "포인트 사용량은 0보다 작을 수 없습니다.")
    @NotNull(message = "포인트 사용량이 입력되지 않았습니다")
    private Integer pointUsage;

    @Positive(message = "총 결제 금액은 1보다 작을 수 없습니다.")
    @NotNull(message = "총 결제 금액이 입력되지 않았습니다")
    private Integer totalPrice;


    public SaleCreateNoBookRequestDto toCreateSaleWithoutBookRequestDto() {
        return new SaleCreateNoBookRequestDto(
                memberId,
                saleOrderName,
                saleOrderContact,
                saleReceiverName,
                saleReceiverContact,
                deliveryFee,
                saleDeliveryDate,
                postcode,
                address,
                detailAddress,
                pointUsage,
                totalPrice
        );
    }
}


