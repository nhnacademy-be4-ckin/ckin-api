package store.ckin.api.member.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.member.domain.MemberAuthRequestDto;
import store.ckin.api.member.domain.MemberAuthResponseDto;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.domain.MemberInfoDetailResponseDto;
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
    public MemberInfoDetailResponseDto getMemberInfoDetail(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException(id);
        }

        return memberRepository.getMemberInfoDetail(id);
    }
}
