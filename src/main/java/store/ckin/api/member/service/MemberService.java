package store.ckin.api.member.service;

import java.util.Optional;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.domain.MemberAuthRequestDto;
import store.ckin.api.member.domain.MemberAuthResponseDto;

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
     * @return LoginResponseDto 로그인 응답 DTO
     */
    Optional<MemberAuthResponseDto> getLoginMemberInfo(MemberAuthRequestDto memberAuthRequestDto);
}
