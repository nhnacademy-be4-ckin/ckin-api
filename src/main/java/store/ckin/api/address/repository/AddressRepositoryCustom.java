package store.ckin.api.address.repository;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;

/**
 * Address Entity 에 Query dsl 을 적용할 메서드를 기술한 interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
@NoRepositoryBean
public interface AddressRepositoryCustom {
    List<MemberAddressResponseDto> getMemberAddressList(Long memberId);
}
