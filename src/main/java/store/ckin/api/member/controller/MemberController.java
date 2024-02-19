package store.ckin.api.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.member.domain.LoginRequestDto;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.exception.LoginFailedException;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.service.MemberService;

/**
 * Member 에 관한 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * Member 생성을 하는 API Method 입니다.
     *
     * @param memberCreateRequestDto Member 생성 요청 DTO
     * @return 201 (Created) : 생성 성공
     */
    @PostMapping("/api/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        memberService.createMember(memberCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 로그인을 처리하는 API Method 입니다.
     *
     * @param loginRequestDto 로그인 정보 요청 DTO
     * @return 200 (OK) : 로그인 정보 확인
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<Void> doLogin(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        memberService.doLogin(loginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * MemberController 에서 MemberAlreadyExistsException 이 발생 시 처리하는 Method 입니다.
     *
     * @param exception MemberAlreadyExistsException
     * @return 409 (Conflict) : 예외 발생 시 계정 생성 실패
     */
    @ExceptionHandler({MemberAlreadyExistsException.class})
    public ResponseEntity<Void> memberAlreadyExistsExceptionHandler(MemberAlreadyExistsException exception) {
        log.debug(exception.getClass().getName() + " : 이미 존재하는 이메일 입니다.");

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * MemberController 에서 LoginFailedException 발생 시 처리하는 Method 입니다.
     *
     * @param exception LoginFailedException
     * @return 403 (Unauthorized) : 로그인 정보 불일치
     */
    @ExceptionHandler({LoginFailedException.class})
    public ResponseEntity<Void> loginFailedExceptionHandler(LoginFailedException exception) {
        log.debug(exception.getClass().getName() + " : 로그인 정보가 일치하지 않습니다.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
