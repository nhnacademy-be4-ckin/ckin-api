package store.ckin.api.sale.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.ckin.api.book.entity.Book;
import store.ckin.api.booksale.entity.BookSale;
import store.ckin.api.common.domain.PageInfo;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;
import store.ckin.api.sale.entity.Sale;

/**
 * 주문 레포지토리 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 07.
 */

@DataJpaTest
class SaleRepositoryTest {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    TestEntityManager entityManager;

    Grade grade;

    Member member;

    String saleNumber;

    BookSale bookSale;

    Book book;

    @BeforeEach
    void setUp() {

        grade = Grade.builder()
                .name("GOLD")
                .pointRatio(10)
                .build();

        entityManager.persist(grade);


        member = Member.builder()
                .grade(grade)
                .email("seungjo@nhn.com")
                .password("1234")
                .name("SEUNGJO")
                .contact("01012341234")
                .birth(LocalDate.of(1999, 5, 13))
                .state(Member.State.ACTIVE)
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .accumulateAmount(10000)
                .build();

        entityManager.persist(member);

        book = Book.builder()
                .bookIsbn("1234567890123")
                .bookDescription("테스트 책입니다.")
                .bookTitle("테스트 책")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.of(2024, 3, 7))
                .bookStock(10)
                .bookRegularPrice(10000)
                .bookDiscountRate(0)
                .bookSalePrice(10000)
                .modifiedAt(LocalDateTime.now())
                .build();

        entityManager.persist(book);

        entityManager.flush();

        saleNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }


    @Test
    @DisplayName("주문 저장 테스트")
    void testSaveSale() {

        Sale sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        Sale actual = saleRepository.save(sale);


        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertEquals(actual.getSaleNumber(), sale.getSaleNumber()),
                () -> assertEquals(actual.getMember().getEmail(), sale.getMember().getEmail()),
                () -> assertEquals(actual.getSaleOrdererName(), sale.getSaleOrdererName()),
                () -> assertEquals(actual.getSaleOrdererContact(), sale.getSaleOrdererContact()),
                () -> assertEquals(actual.getSaleReceiverName(), sale.getSaleReceiverName()),
                () -> assertEquals(actual.getSaleReceiverContact(), sale.getSaleReceiverContact()),
                () -> assertEquals(actual.getSaleReceiverAddress(), sale.getSaleReceiverAddress()),
                () -> assertEquals(actual.getSaleDate(), sale.getSaleDate()),
                () -> assertEquals(actual.getSaleShippingDate(), sale.getSaleShippingDate()),
                () -> assertEquals(actual.getSaleDeliveryDate(), sale.getSaleDeliveryDate()),
                () -> assertEquals(actual.getSaleDeliveryStatus(), sale.getSaleDeliveryStatus()),
                () -> assertEquals(actual.getSaleDeliveryFee(), sale.getSaleDeliveryFee()),
                () -> assertEquals(actual.getSalePointUsage(), sale.getSalePointUsage()),
                () -> assertEquals(actual.getSalePaymentStatus(), sale.getSalePaymentStatus()),
                () -> assertEquals(actual.getSaleShippingPostCode(), sale.getSaleShippingPostCode())
        );
    }

    @Test
    @DisplayName("주문 페이징 조회 테스트")
    void testFindAllByOrderBySaleIdDesc() {
        Sale sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        saleRepository.save(sale);

        saleNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        Sale sale2 = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책2")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        saleRepository.save(sale2);

        List<Sale> actual = saleRepository.findAllByOrderBySaleIdDesc(PageRequest.of(0, 10)).getContent();

        assertEquals(2, actual.size());
    }


    @Test
    @DisplayName("주문 ID로 주문 조회 테스트")
    void testFindBySaleId() {
        Sale sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        Sale savedSale = saleRepository.save(sale);

        SaleResponseDto actual = saleRepository.findBySaleId(savedSale.getSaleId());

        assertAll(
                () -> assertEquals(actual.getSaleId(), savedSale.getSaleId()),
                () -> assertEquals(actual.getSaleNumber(), savedSale.getSaleNumber()),
                () -> assertEquals(actual.getSaleOrdererName(), savedSale.getSaleOrdererName()),
                () -> assertEquals(actual.getSaleOrdererContact(), savedSale.getSaleOrdererContact()),
                () -> assertEquals(actual.getSaleReceiverName(), savedSale.getSaleReceiverName()),
                () -> assertEquals(actual.getSaleReceiverContact(), savedSale.getSaleReceiverContact()),
                () -> assertEquals(actual.getSaleReceiverAddress(), savedSale.getSaleReceiverAddress()),
                () -> assertEquals(actual.getSaleDate(), savedSale.getSaleDate()),
                () -> assertEquals(actual.getSaleShippingDate(), savedSale.getSaleShippingDate()),
                () -> assertEquals(actual.getSaleDeliveryDate(), savedSale.getSaleDeliveryDate()),
                () -> assertEquals(actual.getSaleDeliveryStatus(), savedSale.getSaleDeliveryStatus()),
                () -> assertEquals(actual.getSaleDeliveryFee(), savedSale.getSaleDeliveryFee()),
                () -> assertEquals(actual.getSalePointUsage(), savedSale.getSalePointUsage()),
                () -> assertEquals(actual.getSalePaymentStatus(), savedSale.getSalePaymentStatus()),
                () -> assertEquals(actual.getSaleShippingPostCode(), savedSale.getSaleShippingPostCode())
        );
    }

    @Test
    @DisplayName("주문 ID로 주문 상세 정보와 주문한 책 정보 조회 테스트")
    void testGetSaleWithBook() {
        Sale sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        Sale savedSale = saleRepository.save(sale);


        BookSale.Pk pk = new BookSale.Pk(sale.getSaleId(), book.getBookId());

        bookSale = BookSale.builder()
                .pk(pk)
                .book(book)
                .sale(sale)
                .couponId(1L)
                .bookSalePackagingType("A 포장")
                .bookSalePackagingPrice(1000)
                .bookSaleQuantity(1)
                .bookSalePaymentAmount(10000)
                .build();

        entityManager.persist(bookSale);

        entityManager.flush();

        SaleWithBookResponseDto actual = saleRepository.getSaleWithBook(savedSale.getSaleNumber());


        assertAll(
                () -> assertEquals(actual.getSaleTitle(), book.getBookTitle()),
                () -> assertEquals(actual.getSaleId(), savedSale.getSaleId()),
                () -> assertEquals(actual.getSaleNumber(), savedSale.getSaleNumber()),
                () -> assertEquals(actual.getSaleOrdererName(), savedSale.getSaleOrdererName()),
                () -> assertEquals(actual.getSaleOrdererContact(), savedSale.getSaleOrdererContact()),
                () -> assertEquals(actual.getSaleReceiverName(), savedSale.getSaleReceiverName()),
                () -> assertEquals(actual.getSaleReceiverContact(), savedSale.getSaleReceiverContact()),
                () -> assertEquals(actual.getAddress(), savedSale.getSaleReceiverAddress()),
                () -> assertEquals(actual.getSaleDeliveryDate(), savedSale.getSaleDeliveryDate()),
                () -> assertEquals(actual.getDeliveryFee(), savedSale.getSaleDeliveryFee()),
                () -> assertEquals(actual.getPointUsage(), savedSale.getSalePointUsage()),
                () -> assertEquals(actual.getTotalPrice(), savedSale.getSaleTotalPrice())
        );
    }

    @Test
    @DisplayName("주문 번호로 주문 조회 테스트")
    void testFindBySaleNumber() {
        Sale sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        Sale savedSale = saleRepository.save(sale);

        SaleResponseDto actual = saleRepository.findBySaleNumber(savedSale.getSaleNumber());

        assertAll(
                () -> assertEquals(actual.getSaleId(), savedSale.getSaleId()),
                () -> assertEquals(actual.getSaleNumber(), savedSale.getSaleNumber()),
                () -> assertEquals(actual.getSaleOrdererName(), savedSale.getSaleOrdererName()),
                () -> assertEquals(actual.getSaleOrdererContact(), savedSale.getSaleOrdererContact()),
                () -> assertEquals(actual.getSaleReceiverName(), savedSale.getSaleReceiverName()),
                () -> assertEquals(actual.getSaleReceiverContact(), savedSale.getSaleReceiverContact()),
                () -> assertEquals(actual.getSaleReceiverAddress(), savedSale.getSaleReceiverAddress()),
                () -> assertEquals(actual.getSaleDate(), savedSale.getSaleDate()),
                () -> assertEquals(actual.getSaleShippingDate(), savedSale.getSaleShippingDate()),
                () -> assertEquals(actual.getSaleDeliveryDate(), savedSale.getSaleDeliveryDate()),
                () -> assertEquals(actual.getSaleDeliveryStatus(), savedSale.getSaleDeliveryStatus()),
                () -> assertEquals(actual.getSaleDeliveryFee(), savedSale.getSaleDeliveryFee()),
                () -> assertEquals(actual.getSalePointUsage(), savedSale.getSalePointUsage()),
                () -> assertEquals(actual.getSalePaymentStatus(), savedSale.getSalePaymentStatus()),
                () -> assertEquals(actual.getSaleShippingPostCode(), savedSale.getSaleShippingPostCode())
        );
    }

    @Test
    @DisplayName("회원 ID로 회원의 주문 조회 테스트")
    void testFindAllByMemberId() {

        Sale sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleTitle("테스트 책")
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(Sale.DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(Sale.PaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        entityManager.persist(sale);

        entityManager.flush();

        PagedResponse<List<SaleInfoResponseDto>> pagedResponse =
                saleRepository.findAllByMemberId(member.getId(), Pageable.ofSize(10));

        SaleInfoResponseDto actual = pagedResponse.getData().get(0);
        PageInfo pageInfo = pagedResponse.getPageInfo();

        assertAll(
                () -> assertEquals(sale.getSaleTitle(), actual.getSaleTitle()),
                () -> assertEquals(sale.getSaleNumber(), actual.getSaleNumber()),
                () -> assertEquals(sale.getMember().getEmail(), actual.getMemberEmail()),
                () -> assertEquals(sale.getSaleOrdererName(), actual.getSaleOrdererName()),
                () -> assertEquals(sale.getSaleOrdererContact(), actual.getSaleOrdererContact()),
                () -> assertEquals(sale.getSaleTotalPrice(), actual.getTotalPrice()),
                () -> assertEquals(sale.getSaleDate(), actual.getSaleDate()),
                () -> assertEquals(0, pageInfo.getPage()),
                () -> assertEquals(10, pageInfo.getSize()),
                () -> assertEquals(1, pageInfo.getTotalElements()),
                () -> assertEquals(1, pageInfo.getTotalPages())
        );


    }
}