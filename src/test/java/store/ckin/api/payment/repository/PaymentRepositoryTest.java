package store.ckin.api.payment.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.book.entity.Book;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.payment.dto.response.PaymentResponseDto;
import store.ckin.api.payment.entity.Payment;
import store.ckin.api.payment.entity.PaymentStatus;
import store.ckin.api.sale.entity.DeliveryStatus;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.entity.SalePaymentStatus;

/**
 * 결제 레포지토리 테스트입니다.
 *
 * @author 정승조
 * @version 2024. 03. 11.
 */

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    TestEntityManager entityManager;

    Grade grade;

    Member member;

    String saleNumber;

    Book book;

    Sale sale;

    @BeforeEach
    void setUp() {

        grade = Grade.builder()
                .id(1L)
                .name("GOLD")
                .pointRatio(10)
                .condition(0)
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


        saleNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20);

        sale = Sale.builder()
                .member(member)
                .saleNumber(saleNumber)
                .saleOrdererName("정승조")
                .saleOrdererContact("01012341234")
                .saleReceiverName("정승조")
                .saleReceiverContact("01012341234")
                .saleReceiverAddress("광주광역시 동구 조선대 5길 IT 융합대학")
                .saleDate(LocalDateTime.now())
                .saleShippingDate(LocalDateTime.now())
                .saleDeliveryDate(LocalDate.now().plusDays(2))
                .saleDeliveryStatus(DeliveryStatus.READY)
                .saleDeliveryFee(3000)
                .salePointUsage(1000)
                .salePaymentStatus(SalePaymentStatus.WAITING)
                .saleShippingPostCode("123456")
                .build();

        entityManager.persist(sale);

        entityManager.flush();
    }

    @Test
    @DisplayName("결제 저장 테스트")
    void testSave() {
        Payment payment = Payment.builder()
                .sale(sale)
                .paymentKey("12343214")
                .paymentStatus(PaymentStatus.DONE)
                .requestedAt(LocalDateTime.now().minusMinutes(10))
                .approvedAt(LocalDateTime.now())
                .receipt("https://test.com")
                .build();

        Payment actual = paymentRepository.save(payment);

        assertAll(
                () -> assertNotNull(actual.getPaymentId()),
                () -> assertEquals(actual.getSale(), sale),
                () -> assertEquals(actual.getPaymentKey(), payment.getPaymentKey()),
                () -> assertEquals(actual.getPaymentStatus(), payment.getPaymentStatus()),
                () -> assertNotNull(actual.getRequestedAt()),
                () -> assertNotNull(actual.getApprovedAt()),
                () -> assertEquals(actual.getReceipt(), payment.getReceipt())
        );
    }

    @Test
    @DisplayName("주문 ID로 결제 조회 테스트")
    void testGetPaymentBySaleId() {
        Payment payment = Payment.builder()
                .sale(sale)
                .paymentKey("12343214")
                .paymentStatus(PaymentStatus.DONE)
                .requestedAt(LocalDateTime.now().minusMinutes(10))
                .approvedAt(LocalDateTime.now())
                .receipt("https://test.com")
                .build();

        entityManager.persist(payment);
        entityManager.flush();

        PaymentResponseDto actual = paymentRepository.getPaymentBySaleId(sale.getSaleId());

        assertAll(
                () -> assertNotNull(actual.getPaymentId()),
                () -> assertEquals(actual.getSaleId(), sale.getSaleId()),
                () -> assertEquals(actual.getPaymentKey(), payment.getPaymentKey()),
                () -> assertEquals(actual.getPaymentStatus(), payment.getPaymentStatus()),
                () -> assertNotNull(actual.getRequestedAt()),
                () -> assertNotNull(actual.getApprovedAt()),
                () -> assertEquals(actual.getReceiptUrl(), payment.getReceipt())
        );
    }
}