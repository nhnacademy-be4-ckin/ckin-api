package store.ckin.api.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.request.MemberEmailOnlyRequestDto;
import store.ckin.api.member.domain.request.MemberOauthIdOnlyRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberCannotChangeStateException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.service.MemberService;

/**
 * Member 에 관한 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 존재하는 Email 인지 확인하는 API Method 입니다.
     */
    @PostMapping("/checkEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(
            @Valid @RequestBody MemberEmailOnlyRequestDto memberEmailOnlyRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.alreadyExistsEmail(memberEmailOnlyRequestDto));
    }

    /**
     * Member 생성을 하는 API Method 입니다.
     *
     * @param memberCreateRequestDto Member 생성 요청 DTO
     * @return 201 (Created) : 생성 성공
     */
    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        memberService.createMember(memberCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * JWT 토큰에 필요한 정보 요청을 처리하는 Method 입니다.
     *
     * @param memberAuthRequestDto Member 정보 요청 DTO
     * @return MemberInfoResponseDto Member 정보 응답 DTO (200 OK)
     */
    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponseDto> getMemberInfo(
            @Valid @RequestBody MemberAuthRequestDto memberAuthRequestDto) {
        MemberAuthResponseDto responseDto = memberService.getLoginMemberInfo(memberAuthRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 마이 페이지 정보를 조회하는 Method 입니다.
     *
     * @param id Member ID
     * @return MemberMyPageResponseDto
     */
    @GetMapping("/members/mypage/{memberId}")
    public ResponseEntity<MemberMyPageResponseDto> getMyPageInfo(
            @PathVariable("memberId") Long id) {
        MemberMyPageResponseDto responseDto = memberService.getMyPageInfo(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * OAuth 로그인 시 필요한 정보를 요청하는 API Method 입니다.
     *
     * @param memberOauthIdOnlyRequestDto Member OAuth ID
     * @return MemberOauthLoginResponseDto (200 OK)
     */
    @PostMapping("/login/oauth")
    public ResponseEntity<MemberOauthLoginResponseDto> getOauthMemberInfo(
            @Valid @RequestBody MemberOauthIdOnlyRequestDto memberOauthIdOnlyRequestDto) {
        MemberOauthLoginResponseDto responseDto = memberService.getOauthMemberInfo(memberOauthIdOnlyRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 서비스를 이용할 때마다 최근 로그인 날짜를 갱신해주는 메서드 입니다.
     */
    @PutMapping("/members/{memberId}/update")
    public ResponseEntity<Void> memberUpdateLoginLog(@PathVariable("memberId") Long memberId) {
        memberService.updateLatestLoginAt(memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 계정을 활성화하는 메서드 입니다.
     */
    @PutMapping("/members/{memberId}/active")
    public ResponseEntity<Void> setActiveMember(@PathVariable("memberId") Long memberId) {
        memberService.changeState(memberId, Member.State.ACTIVE);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 휴면 계정으로 전환하는 메서드 입니다.
     */
    @PutMapping("/members/{memberId}/dormant")
    public ResponseEntity<Void> setDormantMember(@PathVariable("memberId") Long memberId) {
        memberService.changeState(memberId, Member.State.DORMANT);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 409 Code 로 응답을 보내는 ExceptionHandler 입니다.
     */
    @ExceptionHandler({MemberAlreadyExistsException.class, MemberCannotChangeStateException.class})
    public ResponseEntity<Void> conflictExceptionHandler(Exception exception) {
        log.debug("{} : {}", exception.getClass().getName(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * 404 Code 로 응답을 보내는 ExceptionHandler 입니다.
     */
    @ExceptionHandler({MemberNotFoundException.class})
    public ResponseEntity<Void> notFoundExceptionHandler(MemberNotFoundException exception) {
        log.debug("{} : {}", exception.getClass().getName(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
