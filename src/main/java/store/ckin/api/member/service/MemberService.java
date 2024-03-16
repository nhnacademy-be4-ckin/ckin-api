package store.ckin.api.member.service;

import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.request.MemberOauthIdOnlyRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;

/**
 * Member 의 관한 로직을 처리하는 서비스 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
public interface MemberService {
    /**
     * Member 를 생성하는 메서드 입니다.
     *
     * @param memberCreateRequestDto Member 생성 요청 DTO
     */
    void createMember(MemberCreateRequestDto memberCreateRequestDto);

    /**
     * 로그인하는 Member 의 정보를 조회하는 메서드 입니다.
     *
     * @param memberAuthRequestDto 로그인 정보 요청 DTO
     */
    MemberAuthResponseDto getLoginMemberInfo(MemberAuthRequestDto memberAuthRequestDto);

    /**
     * 마이페이지 헤더에 들어갈 계정 정보를 조회하는 메서드 입니다.
     *
     * @param id Member ID
     */
    MemberMyPageResponseDto getMyPageInfo(Long id);

    /**
     * 회원의 포인트를 업데이트하는 메서드 입니다.
     *
     * @param memberId   회원 ID
     * @param pointUsage 사용한 포인트
     */
    void updatePoint(Long memberId, Integer pointUsage);

    MemberOauthLoginResponseDto getOauthMemberInfo(MemberOauthIdOnlyRequestDto memberOauthIdOnlyRequestDto);
}
