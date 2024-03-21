package store.ckin.api.member.service;

import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.request.MemberEmailOnlyRequestDto;
import store.ckin.api.member.domain.request.MemberOauthIdOnlyRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;
import store.ckin.api.member.entity.Member;

/**
 * Member 의 관한 로직을 처리하는 서비스 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
public interface MemberService {
    boolean alreadyExistsEmail(MemberEmailOnlyRequestDto memberEmailOnlyRequestDto);

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

    /**
     * [회원 등급의 적립률 * 주문금액]만큼 적립 포인트를 업데이트하는 메서드입니다.
     *
     * @param saleId     주문 ID
     * @param email      회원 이메일
     * @param totalPrice 총 가격
     */
    void updateRewardPoint(Long saleId, String email, Integer totalPrice);

    /**
     * 취소된 주문의 포인트를 업데이트하는 메서드입니다.
     *
     * @param saleId         주문 ID
     * @param memberEmail    회원 이메일
     */
    void updateCancelSalePoint(Long saleId, String memberEmail);

    void updateLatestLoginAt(Long memberId);

    void changeState(Long memberId, Member.State state);
}
