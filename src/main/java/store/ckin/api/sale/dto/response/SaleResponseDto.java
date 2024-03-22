package store.ckin.api.sale.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.ckin.api.sale.entity.DeliveryStatus;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.entity.SalePaymentStatus;

/**
 * 주문 조회 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 03. 03.
 */

@Getter
@AllArgsConstructor
public class SaleResponseDto {

    private Long saleId;

    private Long memberId;

    private String title;

    private String memberEmail;

    private String saleNumber;

    private String saleOrdererName;

    private String saleOrdererContact;

    private String saleReceiverName;

    private String saleReceiverContact;

    private String saleReceiverAddress;

    private LocalDateTime saleDate;

    private LocalDateTime saleShippingDate;

    private LocalDate saleDeliveryDate;

    private DeliveryStatus saleDeliveryStatus;

    private Integer saleDeliveryFee;

    private Integer salePointUsage;

    private Integer saleTotalPrice;

    private SalePaymentStatus salePaymentStatus;

    private String saleShippingPostCode;

    public static SaleResponseDto toDto(Sale sale) {

        return new SaleResponseDto(
                sale.getSaleId(),
                Objects.nonNull(sale.getMember()) ? sale.getMember().getId() : null,
                sale.getSaleTitle(),
                Objects.nonNull(sale.getMember()) ? sale.getMember().getEmail() : "비회원",
                sale.getSaleNumber(),
                sale.getSaleOrdererName(),
                sale.getSaleOrdererContact(),
                sale.getSaleReceiverName(),
                sale.getSaleReceiverContact(),
                sale.getSaleReceiverAddress(),
                sale.getSaleDate(),
                sale.getSaleShippingDate(),
                sale.getSaleDeliveryDate(),
                sale.getSaleDeliveryStatus(),
                sale.getSaleDeliveryFee(),
                sale.getSalePointUsage(),
                sale.getSaleTotalPrice(),
                sale.getSalePaymentStatus(),
                sale.getSaleShippingPostCode()
        );
    }

}
