package store.ckin.api.sale.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.booksale.dto.response.BookSaleResponseDto;
import store.ckin.api.booksale.entity.QBookSale;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.member.entity.QMember;
import store.ckin.api.sale.dto.response.SaleCheckResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.QSale;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.entity.SalePaymentStatus;

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

    /**
     * {@inheritDoc}
     *
     * @param saleId 주문 ID
     * @return 주문 응답 DTO
     */
    @Override
    public SaleResponseDto findBySaleId(Long saleId) {

        QSale sale = QSale.sale;
        QMember member = QMember.member;

        return from(sale)
                .where(sale.saleId.eq(saleId))
                .leftJoin(sale.member, member)
                .on(sale.member.eq(member))
                .select(Projections.constructor(SaleResponseDto.class,
                        sale.saleId,
                        sale.member.id,
                        sale.saleTitle,
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
                        sale.salePaymentStatus,
                        sale.saleShippingPostCode))
                .fetchOne();
    }

    /**
     * {@inheritDoc}
     *
     * @param saleNumber 주문 번호
     * @return 주문 상세 정보와 주문한 책 정보 DTO
     */
    @Override
    public SaleWithBookResponseDto getSaleWithBook(String saleNumber) {

        QSale sale = QSale.sale;
        QBookSale bookSale = QBookSale.bookSale;
        QMember member = QMember.member;


        List<BookSaleResponseDto> bookSaleResponseDtoList =
                from(bookSale)
                        .where(bookSale.sale.saleNumber.eq(saleNumber))
                        .select(Projections.constructor(BookSaleResponseDto.class,
                                bookSale.pk.saleId,
                                bookSale.pk.bookId,
                                bookSale.couponId,
                                bookSale.bookSalePackagingType,
                                bookSale.bookSalePackagingPrice,
                                bookSale.bookSaleQuantity,
                                bookSale.bookSalePaymentAmount))
                        .fetch();


        SaleWithBookResponseDto responseDto =
                from(sale)
                        .where(sale.saleNumber.eq(saleNumber))
                        .leftJoin(sale.member, member)
                        .on(sale.member.id.eq(member.id))
                        .select(Projections.constructor(SaleWithBookResponseDto.class,
                                sale.saleTitle,
                                sale.saleId,
                                sale.saleNumber,
                                sale.member.email,
                                sale.saleOrdererName,
                                sale.saleOrdererContact,
                                sale.saleReceiverName,
                                sale.saleReceiverContact,
                                sale.saleDeliveryFee,
                                sale.saleDeliveryDate,
                                sale.saleDate,
                                sale.saleShippingPostCode,
                                sale.saleReceiverAddress,
                                sale.salePointUsage,
                                sale.saleTotalPrice)
                        ).fetchOne();


        for (BookSaleResponseDto bookSaleResponseDto : bookSaleResponseDtoList) {
            responseDto.addBookSale(bookSaleResponseDto);
        }

        return responseDto;
    }

    /**
     * {@inheritDoc}
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 응답 DTO
     */
    @Override
    public SaleResponseDto findBySaleNumber(String saleNumber) {

        QSale sale = QSale.sale;
        QMember member = QMember.member;

        return from(sale)
                .where(sale.saleNumber.eq(saleNumber))
                .leftJoin(sale.member, member)
                .on(sale.member.eq(member))
                .select(Projections.constructor(SaleResponseDto.class,
                        sale.saleId,
                        sale.member.id,
                        sale.saleTitle,
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
                        sale.salePaymentStatus,
                        sale.saleShippingPostCode))
                .fetchOne();

    }

    /**
     * {@inheritDoc}
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 주문 응답 DTO 리스트
     */
    @Override
    public PagedResponse<List<SaleInfoResponseDto>> findAllByMemberId(Long memberId, Pageable pageable) {

        QSale sale = QSale.sale;

        List<SaleInfoResponseDto> responseDto = from(sale)
                .where(sale.member.id.eq(memberId))
                .select(Projections.constructor(SaleInfoResponseDto.class,
                        sale.saleTitle,
                        sale.saleNumber,
                        sale.member.email,
                        sale.saleOrdererName,
                        sale.saleOrdererContact,
                        sale.saleTotalPrice,
                        sale.saleDate))
                .orderBy(sale.saleId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long totalElements = from(sale)
                .where(sale.member.id.eq(memberId))
                .fetchCount();

        return new PagedResponse<>(
                responseDto,
                PageInfo.builder()
                        .page(pageable.getPageNumber())
                        .size(pageable.getPageSize())
                        .totalElements((int) totalElements)
                        .totalPages((int) Math.ceil((double) totalElements / pageable.getPageSize()))
                        .build());
    }

    @Override
    public SaleCheckResponseDto checkSaleByMemberIdAndBookId(Long memberId, Long bookId) {

        QSale sale = QSale.sale;
        QBookSale bookSale = QBookSale.bookSale;

        boolean isExist = from(sale)
                .join(sale.bookSales, bookSale)
                .where(sale.member.id.eq(memberId)
                        .and(bookSale.book.bookId.eq(bookId))
                        .and(sale.salePaymentStatus.eq(SalePaymentStatus.PAID)))
                .fetchCount() > 0;


        return new SaleCheckResponseDto(isExist);
    }

}
