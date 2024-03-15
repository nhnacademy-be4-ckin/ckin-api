package store.ckin.api.member.repository.impl;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.grade.entity.QGrade;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberInfoDetailResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.entity.QMember;
import store.ckin.api.member.repository.MemberRepositoryCustom;
import store.ckin.api.review.entity.QReview;

/**
 * MemberRepositoryCustom 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
public class MemberRepositoryImpl extends QuerydslRepositorySupport
        implements MemberRepositoryCustom {
    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public MemberAuthResponseDto getLoginInfo(String email) {
        QMember member = QMember.member;

        return from(member)
                .select(Projections.constructor(MemberAuthResponseDto.class,
                        member.id,
                        member.email,
                        member.password,
                        member.role
                ))
                .where(member.email.eq(email))
                .fetchOne();
    }

    @Override
    public MemberInfoDetailResponseDto getMemberInfoDetail(Long id) {
        QMember member = QMember.member;

        return from(member)
                .select(Projections.constructor(MemberInfoDetailResponseDto.class,
                        member.email,
                        member.role))
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public MemberMyPageResponseDto getMyPageInfo(Long id) {
        QMember member = QMember.member;
        QGrade grade = QGrade.grade;
        QReview review = QReview.review;

        return from(member)
                .select(Projections.constructor(MemberMyPageResponseDto.class,
                        member.name,
                        grade.name,
                        member.accumulateAmount,
                        member.point,
                        review.count()))
                .innerJoin(member.grade, grade)
                .leftJoin(review).on(member.id.eq(review.member.id))
                .where(member.id.eq(id))
                .groupBy(member.name, grade.name, member.accumulateAmount, member.point)
                .fetchOne();
    }

}
