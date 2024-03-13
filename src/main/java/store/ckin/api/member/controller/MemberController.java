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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberInfoDetailResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.MemberPointResponseDto;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
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
     * SecurityContextHolder 에 담을 멤버 정보 요청을 처리하는 Method 입니다.
     *
     * @param id Member ID
     * @return MemberInfoDetail
     */
    @PostMapping("/login/{id}")
    public ResponseEntity<MemberInfoDetailResponseDto> getMemberInfoDetail(
            @PathVariable("id") Long id) {
        MemberInfoDetailResponseDto responseDto = memberService.getMemberInfoDetail(id);

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
        log.info("API memberId: {}", id.toString());

        MemberMyPageResponseDto responseDto = memberService.getMyPageInfo(id);

        log.info("API dto : {}", responseDto);
        log.info("API dto name : {}", responseDto.getName());
        log.info("API dto grade name : {}", responseDto.getGradeName());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * MemberController 에서 MemberAlreadyExistsException 이 발생 시 처리하는 Method 입니다.
     *
     * @param exception MemberAlreadyExistsException
     * @return 409 (Conflict) : 예외 발생 시 계정 생성 실패
     */
    @ExceptionHandler({MemberAlreadyExistsException.class})
    public ResponseEntity<Void> memberAlreadyExistsExceptionHandler(MemberAlreadyExistsException exception) {
        log.debug("{} : {}", exception.getClass().getName(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * MemberController 에서 LoginFailedException 발생 시 처리하는 Method 입니다.
     *
     * @param exception LoginFailedException
     * @return 403 (Unauthorized) : 로그인 정보 불일치
     */
    @ExceptionHandler({MemberNotFoundException.class})
    public ResponseEntity<Void> memberNotFoundExceptionHandler(MemberNotFoundException exception) {
        log.debug("{} : {}", exception.getClass().getName(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * 회원의 포인트를 조회하는 메서드 입니다.
     *
     * @param id 회원 ID
     * @return 회원 포인트 응답 DTO
     */
    @GetMapping("/members/{id}/point")
    public ResponseEntity<MemberPointResponseDto> getMemberPoint(@PathVariable("id") Long id) {
        MemberPointResponseDto responseDto = memberService.getMemberPoint(id);

        log.debug("MemberPointResponseDto = {}", responseDto.getPoint());
        return ResponseEntity.ok(responseDto);
    }
}
