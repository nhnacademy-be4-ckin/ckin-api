package store.ckin.api.member.repository;

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.member.domain.MemberInfoResponseDto;

/**
 * Member Entity 에 Query dsl 을 적용할 메서드를 기술한 interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@NoRepositoryBean
public interface MemberRepositoryCustom {
    Optional<MemberInfoResponseDto> getLoginInfo(String email);
}
