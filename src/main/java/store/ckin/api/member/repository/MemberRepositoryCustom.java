package store.ckin.api.member.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.member.domain.response.*;

import java.util.Optional;

/**
 * Member Entity 에 Query dsl 을 적용할 메서드를 기술한 interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@NoRepositoryBean
public interface MemberRepositoryCustom {
    MemberAuthResponseDto getLoginInfo(String email);

    MemberMyPageResponseDto getMyPageInfo(Long id);

    MemberOauthLoginResponseDto getOauthMemberInfo(String oauthId);

    Optional<MemberPasswordResponseDto> getPassword(Long memberId);

    Optional<MemberDetailInfoResponseDto> getMemberDetailInfo(Long memberId);
}
