package store.ckin.api.address.repository.impl;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.entity.Address;
import store.ckin.api.address.entity.QAddress;
import store.ckin.api.address.repository.AddressRepositoryCustom;

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

        return from(address)
                .select(Projections.constructor(MemberAddressResponseDto.class,
                        address.postCode,
                        address.base,
                        address.detail,
                        address.alias,
                        address.isDefault
                        ))
                .where(address.member.id.eq(memberId))
                .fetch();
    }
}
