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
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.sale.dto.response.SaleResponseDto;
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

        entityManager.flush();

        saleNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }


    @Test
    @DisplayName("주문 저장 테스트")
    void testSaveSale() {

        Sale sale = Sale.builder()
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
}