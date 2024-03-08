package store.ckin.api.member.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberInfoDetailResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.MemberPointResponseDto;

/**
 * Member Entity 에 Query dsl 을 적용할 메서드를 기술한 interface 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
@NoRepositoryBean
public interface MemberRepositoryCustom {
    MemberAuthResponseDto getLoginInfo(String email);

    MemberInfoDetailResponseDto getMemberInfoDetail(Long id);

    MemberMyPageResponseDto getMyPageInfo(Long id);
  
    /**
     * 회원 포인트를 조회하는 메서드 입니다.
     *
     * @param id 회원 ID
     * @return 회원 포인트 응답 DTO
     */
    MemberPointResponseDto getMemberPointById(Long id);
}
