package store.ckin.api.review.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.adit.Auditable;
import store.ckin.api.book.entity.Book;
import store.ckin.api.member.entity.Member;

import javax.persistence.*;

/**
 * Review 엔티티 클래스입니다.
 *
 * @author 나국로
 * @version 2024. 03. 01.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Review extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;
    @Column(name = "review_rate")
    private Integer reviewRate;
    @Column(name = "review_comment")
    private String reviewComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
