package store.ckin.api.member.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.request.MemberOauthIdOnlyRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.member.service.MemberService;

/**
 * MemberService interface 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    private final GradeRepository gradeRepository;

    @Transactional
    @Override
    public void createMember(MemberCreateRequestDto memberCreateRequestDto) {
        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new MemberAlreadyExistsException(memberCreateRequestDto.getEmail());
        }

        Grade grade = gradeRepository.findById(1L)
                .orElseThrow(GradeNotFoundException::new);

        Member member = Member.builder()
                .grade(grade)
                .email(memberCreateRequestDto.getEmail())
                .password(memberCreateRequestDto.getPassword())
                .name(memberCreateRequestDto.getName())
                .contact(memberCreateRequestDto.getContact())
                .birth(memberCreateRequestDto.getBirth())
                .state(Member.State.ACTIVE)
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberAuthResponseDto getLoginMemberInfo(MemberAuthRequestDto memberAuthRequestDto) {
        if (!memberRepository.existsByEmail(memberAuthRequestDto.getEmail())) {
            throw new MemberNotFoundException(memberAuthRequestDto.getEmail());
        }

        return memberRepository.getLoginInfo(memberAuthRequestDto.getEmail());
    }

    @Transactional(readOnly = true)
    @Override
    public MemberMyPageResponseDto getMyPageInfo(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException(id);
        }

        return memberRepository.getMyPageInfo(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param memberId   회원 ID
     * @param pointUsage 사용한 포인트
     */
    @Transactional
    @Override
    public void updatePoint(Long memberId, Integer pointUsage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        // TODO : PointHistory - 사용한 포인트 기록 남기기 (추후 구현)
        member.updatePoint(pointUsage);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberOauthLoginResponseDto getOauthMemberInfo(MemberOauthIdOnlyRequestDto memberOauthIdOnlyRequestDto) {
        String oauthId = memberOauthIdOnlyRequestDto.getOauthId();

        if (!memberRepository.existsByOauthId(oauthId)) {
            throw new MemberNotFoundException();
        }

        return memberRepository.getOauthMemberInfo(oauthId);
    }
}
