package store.ckin.api.member.repository.impl;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.member.domain.MemberAuthResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.entity.QMember;
import store.ckin.api.member.repository.MemberRepositoryCustom;

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
}
