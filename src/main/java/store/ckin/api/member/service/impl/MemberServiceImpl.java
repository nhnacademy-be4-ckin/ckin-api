package store.ckin.api.member.service.impl;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.member.service.MemberService;

/**
 * MemberService interface 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public void createMember(MemberCreateRequestDto memberCreateRequestDto) {
        if (!memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new MemberAlreadyExistsException();
        }

        Member member = Member.builder()
                .email(memberCreateRequestDto.getEmail())
                .password(memberCreateRequestDto.getPassword())
                .name(memberCreateRequestDto.getName())
                .contact(memberCreateRequestDto.getContact())
                .birth(memberCreateRequestDto.getBirth())
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .build();

        memberRepository.save(member);
    }
}