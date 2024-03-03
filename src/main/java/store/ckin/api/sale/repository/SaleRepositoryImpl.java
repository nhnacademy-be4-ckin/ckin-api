package store.ckin.api.sale.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.member.entity.QMember;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.entity.QSale;
import store.ckin.api.sale.entity.Sale;

/**
 * 주문 Repository Querydsl 구현 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 03.
 */
public class SaleRepositoryImpl extends QuerydslRepositorySupport implements SaleRepositoryCustom {

    public SaleRepositoryImpl() {
        super(Sale.class);
    }

    @Override
    public List<SaleResponseDto> findAllOrderByIdDesc() {
        QSale sale = QSale.sale;
        QMember member = QMember.member;

        return from(sale)
                .leftJoin(sale.member, member)
                .on(sale.member.eq(member))
                .select(Projections.constructor(SaleResponseDto.class,
                        sale.saleId,
                        sale.member.email,
                        sale.saleNumber,
                        sale.saleOrdererName,
                        sale.saleOrdererContact,
                        sale.saleReceiverName,
                        sale.saleReceiverContact,
                        sale.saleReceiverAddress,
                        sale.saleDate,
                        sale.saleShippingDate,
                        sale.saleDeliveryDate,
                        sale.saleDeliveryStatus,
                        sale.saleDeliveryFee,
                        sale.salePointUsage,
                        sale.saleTotalPrice,
                        sale.salePaymentStatus))
                .orderBy(sale.saleId.desc())
                .fetch();
    }
}
