package store.ckin.api.member.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.request.MemberEmailOnlyRequestDto;
import store.ckin.api.member.domain.request.MemberOauthIdOnlyRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.pointhistory.entity.PointHistory;
import store.ckin.api.pointhistory.exception.PointHistoryNotFoundException;
import store.ckin.api.pointhistory.repository.PointHistoryRepository;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.exception.PointPolicyNotFoundException;
import store.ckin.api.pointpolicy.repository.PointPolicyRepository;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.entity.SalePaymentStatus;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

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

    private final SaleRepository saleRepository;

    private static final Long REGISTER_POINT_POLICY_ID = 100L;

    private static final Long NORMAL_GRADE_ID = 1L;

    @Override
    public boolean alreadyExistsEmail(MemberEmailOnlyRequestDto memberEmailOnlyRequestDto) {
        return memberRepository.existsByEmail(memberEmailOnlyRequestDto.getEmail());
    }

    @Override
    @Transactional
    public void createMember(MemberCreateRequestDto memberCreateRequestDto) {
        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new MemberAlreadyExistsException(memberCreateRequestDto.getEmail());
        }

        Grade grade = gradeRepository.findById(NORMAL_GRADE_ID)
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

        if (Objects.nonNull(memberCreateRequestDto.getOauthId())) {
            member.setOauthId(memberCreateRequestDto.getOauthId());
        }

        Member savedMember = memberRepository.save(member);

        PointHistory pointHistory = PointHistory.builder()
                .member(savedMember)
                .pointHistoryPoint(registerPolicy.getPointPolicyReserve())
                .pointHistoryReason(registerPolicy.getPointPolicyName())
                .pointHistoryTime(LocalDate.now())
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberAuthResponseDto getLoginMemberInfo(MemberAuthRequestDto memberAuthRequestDto) {
        if (!memberRepository.existsByEmail(memberAuthRequestDto.getEmail())) {
            throw new MemberNotFoundException(memberAuthRequestDto.getEmail());
        }

        return memberRepository.getLoginInfo(memberAuthRequestDto.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
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
    @Override
    @Transactional
    public void updatePoint(Long memberId, Integer pointUsage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

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

    /**
     * {@inheritDoc}
     *
     * @param saleId     주문 ID
     * @param email      회원 이메일
     * @param totalPrice 총 가격
     */
    @Override
    @Transactional
    public void updateRewardPoint(Long saleId, String email, Integer totalPrice) {


        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));

        Grade grade = gradeRepository.findById(member.getGrade().getGradeId())
                .orElseThrow(GradeNotFoundException::new);

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        int reward = (int) Math.round(((double) grade.getPointRatio() / 100) * totalPrice);
        member.updatePoint(reward);

        PointHistory pointHistory = PointHistory.builder()
                .member(member)
                .sale(sale)
                .pointHistoryPoint(reward)
                .pointHistoryReason("주문 적립")
                .pointHistoryTime(LocalDate.now())
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    @Override
    @Transactional
    public void updateCancelSalePoint(Long saleId, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException(memberEmail));

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));
        if (sale.getSalePaymentStatus() == SalePaymentStatus.WAITING) {
            // 결제를 하지 않은 주문인 경우
            member.updatePoint(sale.getSalePointUsage());

            PointHistory createPointHistory = PointHistory.builder()
                    .sale(sale)
                    .member(member)
                    .pointHistoryPoint(sale.getSalePointUsage())
                    .pointHistoryTime(LocalDate.now())
                    .pointHistoryReason("주문 취소")
                    .build();

            pointHistoryRepository.save(createPointHistory);
        } else {
            // 결제를 진행한 주문인 경우
            PointHistory pointHistory = pointHistoryRepository.findBySale_SaleId(sale.getSaleId())
                    .orElseThrow(PointHistoryNotFoundException::new);
            Integer pointHistoryPoint = pointHistory.getPointHistoryPoint();

            int totalPoint = (sale.getSaleTotalPrice() + sale.getSalePointUsage()) - pointHistoryPoint;
            member.updatePoint(totalPoint);

            PointHistory createPointHistory = PointHistory.builder()
                    .sale(sale)
                    .member(member)
                    .pointHistoryPoint(totalPoint)
                    .pointHistoryTime(LocalDate.now())
                    .pointHistoryReason("주문 취소")
                    .build();

            pointHistoryRepository.save(createPointHistory);
        }
    }
}
