package store.ckin.api.booksale.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.author.entity.Author;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.category.entity.Category;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.tag.entity.Tag;

/**
 * 주문 도서 레포지토리 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@DataJpaTest
class BookSaleRepositoryTest {

    @Autowired
    BookSaleRepository bookSaleRepository;

    @Autowired
    TestEntityManager entityManager;

    Book book;

    Sale sale;

    Author author;

    Category category;

    Tag tag;

    @BeforeEach
    void setUp() {

        author = Author.builder()
                .authorName("테스트 작가")
                .build();
        entityManager.persist(author);

        tag = Tag.builder().tagName("테스트 태그").build();
        entityManager.persist(tag);

        category = Category.builder()
                .categoryName("테스트 카테고리")
                .categoryPriority(1)
                .build();
        entityManager.persist(category);


        book = Book.builder()
                .bookTitle("테스트 책 제목")
                .bookDescription("테스트 책 설명")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.now())
                .bookRegularPrice(10000)
                .build();

        entityManager.persist(book);

        BookAuthor bookAuthor =
                new BookAuthor(new BookAuthor.PK(book.getBookId(), author.getAuthorId()), book, author);

        entityManager.persist(bookAuthor);

        BookTag bookTag = new BookTag(new BookTag.PK(book.getBookId(), tag.getTagId()), book, tag);
        entityManager.persist(bookTag);

        BookCategory bookCategory =
                new BookCategory(new BookCategory.PK(book.getBookId(), category.getCategoryId()), book, category);

        entityManager.persist(bookCategory);

        sale = Sale.builder()
                .build();

        entityManager.flush();
    }

    @Test
    @DisplayName("주문 도서 저장 테스트")
    void testSaveBookSale() {

        BookSale.Pk pk = new BookSale.Pk(sale.getSaleId(), book.getBookId());
        BookSale bookSale = BookSale.builder()
                .pk(pk)
                .book(book)
                .sale(sale)
                .couponId(1L)
                .bookSaleQuantity(30)
                .bookSalePackagingPrice(3000)
                .bookSalePackagingType("꽃무늬 포장")
                .bookSalePaymentAmount(8000)
                .bookSaleState(BookSale.BookSaleState.ORDER)
                .build();

        BookSale actual = bookSaleRepository.save(bookSale);

        assertAll(
                () -> assertEquals(bookSale.getPk(), actual.getPk()),
                () -> assertEquals(bookSale.getCouponId(), actual.getCouponId()),
                () -> assertEquals(bookSale.getBookSaleQuantity(), actual.getBookSaleQuantity()),
                () -> assertEquals(bookSale.getBookSalePackagingPrice(), actual.getBookSalePackagingPrice()),
                () -> assertEquals(bookSale.getBookSalePackagingType(), actual.getBookSalePackagingType()),
                () -> assertEquals(bookSale.getBookSalePaymentAmount(), actual.getBookSalePaymentAmount()),
                () -> assertEquals(bookSale.getBookSaleState(), actual.getBookSaleState())
        );
    }

    @Test
    @DisplayName("주문 ID를 통한 주문 도서 조회 테스트")
    void testFindAllByPkSaleId() {

        BookSale.Pk pk = new BookSale.Pk(sale.getSaleId(), book.getBookId());
        BookSale bookSale = BookSale.builder()
                .pk(pk)
                .book(book)
                .sale(sale)
                .couponId(1L)
                .bookSaleQuantity(30)
                .bookSalePackagingPrice(3000)
                .bookSalePackagingType("꽃무늬 포장")
                .bookSalePaymentAmount(8000)
                .bookSaleState(BookSale.BookSaleState.ORDER)
                .build();

        BookSale save = bookSaleRepository.save(bookSale);

        assertNotNull(bookSaleRepository.findAllByPkSaleId(save.getSale().getSaleId()));
        assertEquals(1, bookSaleRepository.findAllByPkSaleId(save.getSale().getSaleId()).size());
    }

}