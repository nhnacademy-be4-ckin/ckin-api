package store.ckin.api.member.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.book.entity.Book;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberDetailInfoResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;
import store.ckin.api.member.domain.response.MemberPasswordResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.entity.QMember;
import store.ckin.api.review.entity.Review;

/**
 * MemberRepository 에 대한 Test 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    Grade grade;

    Member member;

    @BeforeEach
    void setUp() {
        grade = Grade.builder()
                .id(1L)
                .name("test")
                .pointRatio(20)
                .condition(0)
                .build();

        entityManager.persist(grade);

        member = Member.builder()
                .grade(grade)
                .email("test@test.com")
                .password("1234")
                .contact("12312312345")
                .birth(LocalDate.now())
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .accumulateAmount(0)
                .build();

        entityManager.persist(member);
    }

    @Test
    @DisplayName("이메일로 계정 존재 여부 확인")
    void testExistsByEmail() {
        assertTrue(memberRepository.existsByEmail(member.getEmail()));
        assertFalse(memberRepository.existsByEmail("false@test.com"));
    }

    @Test
    @DisplayName("OAuthID 로 계정 존재 여부 확인")
    void testExistsByOauthId() {
        Member oauthMember = Member.builder()
                .grade(grade)
                .email("oauthTest@test.com")
                .password("1234")
                .contact("12367891234")
                .birth(LocalDate.now())
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .oauthId("oauthId123")
                .build();

        memberRepository.save(oauthMember);

        assertTrue(memberRepository.existsByOauthId(oauthMember.getOauthId()));
        assertFalse(memberRepository.existsByOauthId("oauthId124"));
    }

    @Test
    @DisplayName("이메일로 계정 조회")
    void testFindByEmail() {
        assertTrue(memberRepository.findByEmail(member.getEmail()).isPresent());
        assertTrue(memberRepository.findByEmail("false@test.com").isEmpty());
    }

    @Test
    @DisplayName("이메일로 로그인에 필요한 정보를 조회")
    void testGetLoginInfo() {
        MemberAuthResponseDto result =
                memberRepository.getLoginInfo(member.getEmail());

        assertEquals(member.getId(), result.getId());
        assertEquals(member.getEmail(), result.getEmail());
        assertEquals(member.getPassword(), result.getPassword());
        assertEquals(member.getRole().name(), result.getRole());
    }

    @Test
    @DisplayName("멤버 ID로 마이페이지에 필요한 정보 조회")
    void testMyPageInfo() {
        Book book = Book.builder()
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

        Review review = Review.builder()
                .reviewRate(5)
                .reviewComment("Good")
                .member(member)
                .book(book)
                .build();

        entityManager.persist(review);

        MemberMyPageResponseDto result =
                memberRepository.getMyPageInfo(member.getId());

        assertEquals(member.getName(), result.getName());
        assertEquals(grade.getName(), result.getGradeName());
        assertEquals(member.getAccumulateAmount(), result.getAccumulateAmount());
        assertEquals(member.getPoint(), result.getPoint());
        assertEquals(grade.getCondition(), result.getGradeCondition());
        assertEquals(1, result.getCountReview());
    }

    @Test
    @DisplayName("OAuth ID 로 소셜 로그인에 필요한 정보 조회")
    void testGetOauthMemberInfo() {
        Member oauthMember = Member.builder()
                .grade(grade)
                .email("oauthTest@test.com")
                .password("1234")
                .contact("12367891234")
                .birth(LocalDate.now())
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .oauthId("oauthId123")
                .build();

        memberRepository.save(oauthMember);

        MemberOauthLoginResponseDto result =
                memberRepository.getOauthMemberInfo(oauthMember.getOauthId());

        assertEquals(oauthMember.getId(), result.getId());
        assertEquals(oauthMember.getRole().toString(), result.getRole());
    }

    @Test
    @DisplayName("회원 ID로 회원 비밀번호 조회")
    void testGetPassword() {

        Optional<MemberPasswordResponseDto> password = memberRepository.getPassword(member.getId());

        assertTrue(password.isPresent());

        MemberPasswordResponseDto actual = password.get();

        assertAll(
                () -> assertEquals(member.getPassword(), actual.getPassword())
        );
    }

    @Test
    @DisplayName("회원 ID로 회원 상세 정보 조회")
    void testGetMemberDetailInfo() {

        Optional<MemberDetailInfoResponseDto> memberDetail = memberRepository.getMemberDetailInfo(member.getId());

        assertTrue(memberDetail.isPresent());

        MemberDetailInfoResponseDto actual = memberDetail.get();

        assertAll(
                () -> assertEquals(member.getName(), actual.getName()),
                () -> assertEquals(member.getContact(), actual.getContact()),
                () -> assertEquals(member.getBirth(), actual.getBirth())
        );
    }
}