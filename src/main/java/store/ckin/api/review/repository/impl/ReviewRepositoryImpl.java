package store.ckin.api.review.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.book.entity.QBook;
import store.ckin.api.member.entity.QMember;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.entity.QReview;
import store.ckin.api.review.entity.Review;
import store.ckin.api.review.repository.ReviewRepositoryCustom;

/**
 * ReviewRepository 구현클래스.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom {
    private final EntityManager entityManager;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        super(Review.class);
        this.entityManager = entityManager;
    }

    QReview review = QReview.review;
    QMember member = QMember.member;

    QBook book = QBook.book;

    @Override
    public Page<ReviewResponseDto> findReviewsByMemberWithPagination(Long memberId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);


        List<Review> reviews = queryFactory
                .selectFrom(review)
                .leftJoin(review.member, member)
                .where(member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(review.count())
                        .from(review)
                        .leftJoin(review.member, member)
                        .where(member.id.eq(memberId))
                        .fetchOne())
                .orElse(0L);


        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(this::convertToReviewResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewResponseDtos, pageable, total);
    }

    @Override
    public Page<ReviewResponseDto> findReviewsByBookWithPagination(Long bookId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Review> reviews = queryFactory
                .selectFrom(review)
                .leftJoin(review.book, book)
                .where(book.bookId.eq(bookId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(review.count())
                        .from(review)
                        .leftJoin(review.book, book)
                        .where(book.bookId.eq(bookId))
                        .fetchOne())
                .orElse(0L);

        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(this::convertToReviewResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewResponseDtos, pageable, total);
    }



    private ReviewResponseDto convertToReviewResponseDto(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getReviewComment(),
                review.getCreatedAt(),
                review.getMember() != null ? review.getMember().getName() : null);
    }



}