package store.ckin.api.review.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.book.entity.Book;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.entity.Review;
import store.ckin.api.review.repository.ReviewRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ReviewRepositoryTest
 *
 * @author : 이가은
 * @version : 2024. 03. 12
 */
@DataJpaTest
@Import(ReviewRepositoryImpl.class)
class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private Review review;
    private Member member;
    private Book book;
    private Grade grade;
    private Pageable pageable;
    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.now();

        member = Member.builder()
                .grade(grade)
                .birth(LocalDate.of(2023, 9, 1))
                .email("ckin1234@naver.com")
                .password("ckin1234")
                .name("ckin")
                .contact("010-1234-5678")
                .point(500)
                .state(Member.State.ACTIVE)
                .accumulateAmount(30000)
                .role(Member.Role.MEMBER)
                .build();
        testEntityManager.persist(member);

        book = Book.builder()
                .bookTitle("사람은 무엇으로 사는가")
                .bookIsbn("1234567890123")
                .bookDescription("<p>이 책의 설명입니다.<p>")
                .bookPublisher("ckin출판사")
                .bookIndex("이 책의 목차입니다.")
                .bookPackaging(true)
                .bookState("ON_SALE")
                .bookStock(180)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookSalePrice(18000)
                .bookReviewRate("4.3")
                .modifiedAt(LocalDateTime.now())
                .build();

        testEntityManager.persist(book);
        testEntityManager.flush();

        review = new Review(1L, 5, "good", member, book);

        reviewRepository.save(review);
        pageable = Pageable.ofSize(5);
    }

    @Test
    @DisplayName("멤버 아이디로 리뷰 목록 조회 테스트")
    void testFindReviewsByMemberWithPagination() {
        Page<ReviewResponseDto> results = reviewRepository.findReviewsByMemberWithPagination(1L, pageable);

        Assertions.assertThat(results.getNumber()).isEqualTo(pageable.getPageNumber());
        Assertions.assertThat(results.getContent().get(0).getReviewRate()).isEqualTo(review.getReviewRate());
        Assertions.assertThat(results.getContent().get(0).getReviewDate()).isEqualTo(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Assertions.assertThat(results.getContent().get(0).getMessage()).isEqualTo(review.getReviewComment());
        Assertions.assertThat(results.getContent().get(0).getAuthor()).isEqualTo("***" + review.getMember().getEmail().substring(3));
        Assertions.assertThat(results.getContent().get(0).getReviewId()).isNotNull();
    }

    @Test
    @DisplayName("도서 아이디로 리뷰 목록 조회 테스트")
    void testGetReviewPageList() {
        Page<ReviewResponseDto> results = reviewRepository.getReviewPageList(pageable, book.getBookId());

        Assertions.assertThat(results.getNumber()).isEqualTo(pageable.getPageNumber());
        Assertions.assertThat(results.getContent().get(0).getReviewRate()).isEqualTo(review.getReviewRate());
        Assertions.assertThat(results.getContent().get(0).getReviewDate()).isEqualTo(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Assertions.assertThat(results.getContent().get(0).getMessage()).isEqualTo(review.getReviewComment());
        Assertions.assertThat(results.getContent().get(0).getAuthor()).isEqualTo("***" + review.getMember().getEmail().substring(3));
        Assertions.assertThat(results.getContent().get(0).getReviewId()).isNotNull();
    }

}