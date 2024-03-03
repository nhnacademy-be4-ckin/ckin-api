package store.ckin.api.booksale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.booksale.entity.BookSale;

/**
 * 주문 도서 (리스트) JPA 레포지토리입니다.
 *
 * @author 정승조
 * @version 2024. 03. 02.
 */

public interface BookSaleRepository extends JpaRepository<BookSale, BookSale.Pk> {
}
