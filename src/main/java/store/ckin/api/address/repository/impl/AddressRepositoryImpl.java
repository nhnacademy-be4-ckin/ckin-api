package store.ckin.api.address.repository.impl;

import java.util.List;

import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.entity.Address;
import store.ckin.api.address.entity.QAddress;
import store.ckin.api.address.repository.AddressRepositoryCustom;
import store.ckin.api.member.entity.QMember;

/**
 * MemberRepositoryCustom 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
public class AddressRepositoryImpl extends QuerydslRepositorySupport
        implements AddressRepositoryCustom {
    public AddressRepositoryImpl() {
        super(Address.class);
    }

    @Override
    public List<MemberAddressResponseDto> getMemberAddressList(Long memberId) {
        QAddress address = QAddress.address;
        QMember member = QMember.member;

        return from(address)
                .select(Projections.constructor(MemberAddressResponseDto.class,
                        address.base,
                        address.detail,
                        address.alias,
                        address.isDefault
                        ))
                .where(member.id.eq(memberId))
                .fetch();
    }
}
