package store.ckin.api.address.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.entity.Address;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;

/**
 * AddressRepository 테스트 코드 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@DataJpaTest
class AddressRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        Grade grade = Grade.builder()
                .id(1L)
                .name("일반")
                .pointRatio(10)
                .condition(0)
                .build();

        entityManager.persist(grade);

        member = Member.builder()
                .grade(grade)
                .email("test@nhn.com")
                .password("1234")
                .name("tester")
                .contact("01012341234")
                .birth(LocalDate.of(2024, 1, 1))
                .state(Member.State.ACTIVE)
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .accumulateAmount(0)
                .build();

        entityManager.persist(member);
    }

    @Test
    @DisplayName("중복된 주소가 있는지 확인")
    void testDuplicateAddress() {
        Address address = Address.builder()
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .build();

        addressRepository.save(address);

        assertTrue(addressRepository.existsByMemberIdAndBaseAndDetail(
                member.getId(),
                "광주",
                "광역시"
        ));
        assertFalse(addressRepository.existsByMemberIdAndBaseAndDetail(
                member.getId(),
                "광주",
                "광산구"
        ));
    }

    @Test
    @DisplayName("특정 멤버 주소 중 기본 주소가 있는지 확인")
    void testFindDefaultAddressByMemberId() {
        Address address = Address.builder()
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .isDefault(false)
                .build();

        addressRepository.save(address);


        assertTrue(addressRepository
                .findDefaultAddressByMemberId(member.getId())
                .isEmpty());

        address.toggleDefault();
        entityManager.merge(address);

        assertTrue(addressRepository
                .findDefaultAddressByMemberId(member.getId())
                .isPresent());
    }

    @Test
    @DisplayName("주소 ID와 멤버 ID로 주소 조회")
    void testFindByIdAndMemberId() {
        Address address = Address.builder()
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .build();

        Address savedAddress = addressRepository.save(address);

        assertTrue(addressRepository
                .findByIdAndMember_Id(savedAddress.getId(), member.getId())
                .isPresent());
        assertTrue(addressRepository
                .findByIdAndMember_Id(savedAddress.getId(), member.getId() + 1L)
                .isEmpty());
    }

    @Test
    @DisplayName("주소 ID와 멤버 ID로 주소 존재 여부 확인")
    void testExistsByIdAndMemberId() {
        Address address = Address.builder()
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .build();

        Address savedAddress = addressRepository.save(address);

        assertTrue(addressRepository
                .existsByIdAndMember_Id(savedAddress.getId(), member.getId()));
        assertFalse(addressRepository
                .existsByIdAndMember_Id(savedAddress.getId(), member.getId() + 1L));
    }

    @Test
    @DisplayName("회원 ID로 주소 목록 조회")
    void testGetMemberAddressList() {
        Address address = Address.builder()
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .build();

        addressRepository.save(address);

        List<MemberAddressResponseDto> actualList = addressRepository.getMemberAddressList(member.getId());

        assertFalse(actualList.isEmpty());

        MemberAddressResponseDto actual = actualList.get(0);

        assertAll(
                () -> assertNotNull(actual.getAddressId()),
                () -> assertEquals(actual.getPostCode(), address.getPostCode()),
                () -> assertEquals(actual.getBase(), address.getBase()),
                () -> assertEquals(actual.getDetail(), address.getDetail()),
                () -> assertEquals(actual.getAlias(), address.getAlias())
        );
    }
}