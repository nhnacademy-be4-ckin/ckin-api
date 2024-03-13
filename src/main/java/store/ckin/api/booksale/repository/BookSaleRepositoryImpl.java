package store.ckin.api.booksale.repository;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.booksale.dto.response.BookAndBookSaleResponseDto;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.booksale.entity.QBookSale;
import store.ckin.api.file.entity.QFile;

import java.util.List;

/**
 * 도서 주문 리스트 Querydsl 레포지토리 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 12.
 */
public class BookSaleRepositoryImpl extends QuerydslRepositorySupport implements BookSaleRepositoryCustom {

    public BookSaleRepositoryImpl() {
        super(BookSale.class);
    }

    @Override
    public List<BookAndBookSaleResponseDto> getBookSaleDetailBySaleId(Long saleId) {


        QBookSale bookSale = QBookSale.bookSale;
        QFile file = QFile.file;

        return from(bookSale)
                .join(bookSale.book.thumbnail, file)
                .where(bookSale.sale.saleId.eq(saleId))
                .select(Projections.constructor(BookAndBookSaleResponseDto.class,
                        bookSale.book.bookId,
                        bookSale.book.thumbnail.fileUrl,
                        bookSale.book.bookTitle,
                        bookSale.bookSaleQuantity,
                        bookSale.couponId,
                        bookSale.bookSalePackagingType,
                        bookSale.bookSalePackagingPrice,
                        bookSale.bookSalePaymentAmount))
                .fetch();
    }
}
