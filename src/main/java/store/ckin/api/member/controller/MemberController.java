package store.ckin.api.member.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.service.MemberService;

/**
 * Member 에 관한 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Member 생성을 하는 API METHOD 입니다.
     *
     * @param memberCreateRequestDto Member 생성 요청 DTO
     * @return 201 (Created) : 생성 성공 / 409 (Conflict) : 중복된 이메일일 경우 생성 실패
     */
    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        memberService.createMember(memberCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
