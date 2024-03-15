package store.ckin.api.member.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberInfoDetailResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.pointhistory.entity.PointHistory;
import store.ckin.api.pointhistory.repository.PointHistoryRepository;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.exception.PointPolicyNotFoundException;
import store.ckin.api.pointpolicy.repository.PointPolicyRepository;

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

    private final PointHistoryRepository pointHistoryRepository;

    private final PointPolicyRepository pointPolicyRepository;

    private static final Long REGISTER_POINT_POLICY_ID = 100L;

    @Transactional
    @Override
    public void createMember(MemberCreateRequestDto memberCreateRequestDto) {
        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new MemberAlreadyExistsException(memberCreateRequestDto.getEmail());
        }

        Grade grade = gradeRepository.findById(1L)
                .orElseThrow(GradeNotFoundException::new);

        // 회원가입 포인트 정책 조회
        PointPolicy registerPolicy = pointPolicyRepository.findById(REGISTER_POINT_POLICY_ID)
                .orElseThrow(() -> new PointPolicyNotFoundException(REGISTER_POINT_POLICY_ID));


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
                .point(registerPolicy.getPointPolicyReserve())
                .build();

        Member savedMember = memberRepository.save(member);

        PointHistory pointHistory = PointHistory.builder()
                .member(savedMember)
                .pointHistoryPoint(registerPolicy.getPointPolicyReserve())
                .pointHistoryReason(registerPolicy.getPointPolicyName())
                .pointHistoryTime(LocalDate.now())
                .build();

        pointHistoryRepository.save(pointHistory);
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
        member.updatePoint(pointUsage);

        pointHistoryRepository.save(PointHistory.builder()
                .member(member)
                .pointHistoryPoint(-pointUsage)
                .pointHistoryReason("주문 사용")
                .pointHistoryTime(LocalDate.now())
                .build());
    }

    @Override
    public void updateRewardPoint(Long memberId, Integer totalPrice) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Grade grade = gradeRepository.findById(member.getGrade().getGradeId())
                .orElseThrow(GradeNotFoundException::new);

        long reward = Math.round(((double) grade.getPointRatio() / 100) * totalPrice);
        member.updatePoint((int) reward);
    }
}
