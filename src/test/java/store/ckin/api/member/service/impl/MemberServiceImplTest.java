package store.ckin.api.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.member.domain.request.MemberAuthRequestDto;
import store.ckin.api.member.domain.request.MemberCreateRequestDto;
import store.ckin.api.member.domain.request.MemberEmailOnlyRequestDto;
import store.ckin.api.member.domain.request.MemberOauthIdOnlyRequestDto;
import store.ckin.api.member.domain.request.MemberPasswordRequestDto;
import store.ckin.api.member.domain.request.MemberUpdateRequestDto;
import store.ckin.api.member.domain.response.MemberAuthResponseDto;
import store.ckin.api.member.domain.response.MemberDetailInfoResponseDto;
import store.ckin.api.member.domain.response.MemberMyPageResponseDto;
import store.ckin.api.member.domain.response.MemberOauthLoginResponseDto;
import store.ckin.api.member.domain.response.MemberPasswordResponseDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberCannotChangeStateException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.exception.MemberOauthNotFoundException;
import store.ckin.api.member.exception.MemberPasswordCannotChangeException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.pointhistory.entity.PointHistory;
import store.ckin.api.pointhistory.repository.PointHistoryRepository;
import store.ckin.api.pointpolicy.entity.PointPolicy;
import store.ckin.api.pointpolicy.repository.PointPolicyRepository;
import store.ckin.api.sale.entity.Sale;
import store.ckin.api.sale.entity.SalePaymentStatus;
import store.ckin.api.sale.exception.SaleNotFoundException;
import store.ckin.api.sale.repository.SaleRepository;

/**
 * MemberService 에 대한 Test 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    GradeRepository gradeRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    PointPolicyRepository pointPolicyRepository;

    @Mock
    SaleRepository saleRepository;

    static final Long REGISTER_POINT_POLICY_ID = 100L;


    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재")
    void testAlreadyExistsEmail_True() {
        MemberEmailOnlyRequestDto emailDto = new MemberEmailOnlyRequestDto();
        ReflectionTestUtils.setField(emailDto, "email", "test@test.com");

        given(memberRepository.existsByEmail(anyString()))
                .willReturn(true);

        boolean actual = memberService.alreadyExistsEmail(emailDto);

        assertTrue(actual);

        verify(memberRepository).existsByEmail(anyString());
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하지 않음")
    void testAlreadyExistsEmail_Fail() {
        MemberEmailOnlyRequestDto emailDto = new MemberEmailOnlyRequestDto();
        ReflectionTestUtils.setField(emailDto, "email", "test@test.com");

        given(memberRepository.existsByEmail(anyString()))
                .willReturn(false);

        boolean actual = memberService.alreadyExistsEmail(emailDto);

        assertFalse(actual);

        verify(memberRepository).existsByEmail(anyString());
    }


    @Test
    @DisplayName("멤버 생성 성공 테스트 - 일반 회원")
    void testCreateMemberSuccess_Normal() {
        String testEmail = "test@test.com";
        Grade grade = Grade.builder().build();

        when(memberRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        MemberCreateRequestDto dto = new MemberCreateRequestDto();

        ReflectionTestUtils.setField(dto, "email", testEmail);
        ReflectionTestUtils.setField(dto, "password", "pwd");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());

        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(100L)
                .pointPolicyName("회원가입")
                .pointPolicyReserve(5000)
                .build();

        when(pointPolicyRepository.findById(REGISTER_POINT_POLICY_ID))
                .thenReturn(Optional.of(pointPolicy));

        memberService.createMember(dto);

        verify(memberRepository).existsByEmail(testEmail);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("멤버 생성 성공 테스트 - Oauth 회원")
    void testCreateMemberSuccess_Oauth() {
        String testEmail = "test@test.com";
        Grade grade = Grade.builder().build();

        when(memberRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        MemberCreateRequestDto dto = new MemberCreateRequestDto();

        ReflectionTestUtils.setField(dto, "email", testEmail);
        ReflectionTestUtils.setField(dto, "password", "pwd");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());
        ReflectionTestUtils.setField(dto, "oauthId", "1234");

        PointPolicy pointPolicy = PointPolicy.builder()
                .pointPolicyId(100L)
                .pointPolicyName("회원가입")
                .pointPolicyReserve(5000)
                .build();

        when(pointPolicyRepository.findById(REGISTER_POINT_POLICY_ID))
                .thenReturn(Optional.of(pointPolicy));

        memberService.createMember(dto);

        verify(memberRepository).existsByEmail(testEmail);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("멤버 생성 실패 시 MemberAlreadyExistsException 호출")
    void testCreateMemberFailed() {
        String testEmail = "test@test.com";

        when(memberRepository.existsByEmail(anyString()))
                .thenReturn(true);

        MemberCreateRequestDto dto = new MemberCreateRequestDto();

        ReflectionTestUtils.setField(dto, "email", testEmail);
        ReflectionTestUtils.setField(dto, "password", "pwd");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());

        assertThrows(MemberAlreadyExistsException.class,
                () -> memberService.createMember(dto));

        verify(memberRepository).existsByEmail(testEmail);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인한 회원 정보 조회 - 실패 (존재하지 않는 회원)")
    void testGetLoginMemberInfo_NotFound() {

        MemberAuthRequestDto memberAuthRequestDto = new MemberAuthRequestDto();
        ReflectionTestUtils.setField(memberAuthRequestDto, "email", "test@test.com");

        given(memberRepository.existsByEmail(anyString()))
                .willReturn(false);

        assertThrows(MemberNotFoundException.class,
                () -> memberService.getLoginMemberInfo(memberAuthRequestDto));

        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(0)).getLoginInfo(anyString());

    }

    @Test
    @DisplayName("로그인한 회원 정보 조회 - 성공")
    void testGetLoginMemberInfo_Success() {

        MemberAuthRequestDto memberAuthRequestDto = new MemberAuthRequestDto();
        ReflectionTestUtils.setField(memberAuthRequestDto, "email", "test@test.com");

        given(memberRepository.existsByEmail(anyString()))
                .willReturn(true);

        MemberAuthResponseDto memberAuthResponseDto
                = new MemberAuthResponseDto(1L, "test@test.com", "1234", Member.Role.MEMBER);

        given(memberRepository.getLoginInfo(anyString()))
                .willReturn(memberAuthResponseDto);

        MemberAuthResponseDto actual = memberService.getLoginMemberInfo(memberAuthRequestDto);

        assertAll(
                () -> assertEquals(memberAuthResponseDto.getId(), actual.getId()),
                () -> assertEquals(memberAuthResponseDto.getEmail(), actual.getEmail()),
                () -> assertEquals(memberAuthResponseDto.getPassword(), actual.getPassword()),
                () -> assertEquals(memberAuthResponseDto.getRole(), actual.getRole())
        );


        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(1)).getLoginInfo(anyString());
    }

    @Test
    @DisplayName("마이페이지 정보 조회 - 실패 (존재하지 않는 회원)")
    void testGetMyPageInfo_False() {

        Long memberId = 1L;

        given(memberRepository.existsById(anyLong()))
                .willReturn(false);

        assertThrows(MemberNotFoundException.class,
                () -> memberService.getMyPageInfo(memberId));

        verify(memberRepository, times(1)).existsById(anyLong());
        verify(memberRepository, times(0)).getMyPageInfo(anyLong());
    }


    @Test
    @DisplayName("마이페이지 정보 조회 - 성공")
    void testGetMyPageInfo_Success() {

        Long memberId = 1L;

        given(memberRepository.existsById(anyLong()))
                .willReturn(true);

        MemberMyPageResponseDto memberMyPageResponseDto
                = new MemberMyPageResponseDto("테스터", "MEMBER", 10000, 5000, 0, 0L);

        given(memberRepository.getMyPageInfo(anyLong()))
                .willReturn(memberMyPageResponseDto);

        MemberMyPageResponseDto actual = memberService.getMyPageInfo(memberId);

        assertAll(
                () -> assertEquals(memberMyPageResponseDto.getName(), actual.getName()),
                () -> assertEquals(memberMyPageResponseDto.getGradeName(), actual.getGradeName()),
                () -> assertEquals(memberMyPageResponseDto.getAccumulateAmount(), actual.getAccumulateAmount()),
                () -> assertEquals(memberMyPageResponseDto.getPoint(), actual.getPoint()),
                () -> assertEquals(memberMyPageResponseDto.getGradeCondition(), actual.getGradeCondition()),
                () -> assertEquals(memberMyPageResponseDto.getCountReview(), actual.getCountReview())
        );


        verify(memberRepository, times(1)).existsById(anyLong());
        verify(memberRepository, times(1)).getMyPageInfo(anyLong());
    }

    @Test
    @DisplayName("회원 포인트 수정 - 실패 (존재하지 않는 회원)")
    void testUpdatePoint_NotFound() {

        Long memberId = 1L;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.updatePoint(memberId, 1000));

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 포인트 수정 - 성공")
    void testUpdatePoint_Sucess() {

        Long memberId = 1L;

        Member member = Member.builder()
                .point(1000)
                .build();

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        memberService.updatePoint(memberId, 100);

        assertEquals(1100, member.getPoint());

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Oauth 회원 정보 조회 - 실패 (존재하지 않는 회원)")
    void testGetOauthMemberInfo_NotFound() {
        MemberOauthIdOnlyRequestDto memberOauth = new MemberOauthIdOnlyRequestDto();
        ReflectionTestUtils.setField(memberOauth, "oauthId", "1234");

        given(memberRepository.existsByOauthId(anyString()))
                .willReturn(false);

        assertThrows(MemberOauthNotFoundException.class,
                () -> memberService.getOauthMemberInfo(memberOauth));

        verify(memberRepository, times(1)).existsByOauthId(anyString());
        verify(memberRepository, times(0)).getOauthMemberInfo(anyString());
    }

    @Test
    @DisplayName("Oauth 회원 정보 조회 - 성공")
    void testGetOauthMemberInfo_Success() {
        MemberOauthIdOnlyRequestDto memberOauth = new MemberOauthIdOnlyRequestDto();
        ReflectionTestUtils.setField(memberOauth, "oauthId", "1234");

        given(memberRepository.existsByOauthId(anyString()))
                .willReturn(true);

        MemberOauthLoginResponseDto memberOauthLoginResponseDto
                = new MemberOauthLoginResponseDto(1L, Member.Role.MEMBER);

        given(memberRepository.getOauthMemberInfo(anyString()))
                .willReturn(memberOauthLoginResponseDto);

        MemberOauthLoginResponseDto actual = memberService.getOauthMemberInfo(memberOauth);

        assertAll(
                () -> assertEquals(memberOauthLoginResponseDto.getId(), actual.getId()),
                () -> assertEquals(memberOauthLoginResponseDto.getRole(), actual.getRole()
                ));


        verify(memberRepository, times(1)).existsByOauthId(anyString());
        verify(memberRepository, times(1)).getOauthMemberInfo(anyString());
    }


    @Test
    @DisplayName("회원 적립금 수정 - 실패 (존재하지 않는 회원)")
    void testUpdateRewardPoint_MemberNotFound() {
        Long saleId = 1L;
        String email = "test@test.com";
        Integer totalPrice = 10000;

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.updateRewardPoint(saleId, email, totalPrice));

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(gradeRepository, times(0)).findById(anyLong());
        verify(saleRepository, times(0)).findById(anyLong());
        verify(pointHistoryRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("회원 적립금 수정 - 실패 (회원 등급이 없음)")
    void testUpdateRewardPoint_GradeNotFound() {
        Long saleId = 1L;
        String email = "test@test.com";
        Integer totalPrice = 10000;

        Grade grade = Grade.builder()
                .id(1L)
                .name("MEMBER")
                .pointRatio(10)
                .condition(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .grade(grade)
                .point(1000)
                .build();

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        given(gradeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(GradeNotFoundException.class,
                () -> memberService.updateRewardPoint(saleId, email, totalPrice));

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(gradeRepository, times(1)).findById(anyLong());
        verify(saleRepository, times(0)).findById(anyLong());
        verify(pointHistoryRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("회원 적립금 수정 - 실패 (주문 정보가 존재하지 않는 경우)")
    void testUpdateRewardPoint_SaleNotFound() {
        Long saleId = 1L;
        String email = "test@test.com";
        Integer totalPrice = 10000;

        Grade grade = Grade.builder()
                .id(1L)
                .name("MEMBER")
                .pointRatio(10)
                .condition(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .grade(grade)
                .point(1000)
                .build();

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        given(gradeRepository.findById(anyLong()))
                .willReturn(Optional.of(grade));

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class,
                () -> memberService.updateRewardPoint(saleId, email, totalPrice));

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(gradeRepository, times(1)).findById(anyLong());
        verify(saleRepository, times(1)).findById(anyLong());
        verify(pointHistoryRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("회원 적립금 수정 - 성공")
    void testUpdateRewardPoint_Success() {
        Long saleId = 1L;
        String email = "test@test.com";
        Integer totalPrice = 10000;

        Grade grade = Grade.builder()
                .id(1L)
                .name("MEMBER")
                .pointRatio(10)
                .condition(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .grade(grade)
                .point(1000)
                .build();

        Sale sale = Sale.builder()
                .member(member)
                .build();

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        given(gradeRepository.findById(anyLong()))
                .willReturn(Optional.of(grade));

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        memberService.updateRewardPoint(saleId, email, totalPrice);

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(gradeRepository, times(1)).findById(anyLong());
        verify(saleRepository, times(1)).findById(anyLong());
        verify(pointHistoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문 취소 시 적립금 회수 - 실패 (존재하지 않는 회원)")
    void testUpdateCancelRewardPoint_MemberNotFound() {
        Long saleId = 1L;
        String email = "test@test.com";

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.updateCancelSalePoint(saleId, email));

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(saleRepository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 취소시 적립금 회수 - 실패 (존재하지 않는 주문)")
    void testUpdateCancelRewardPoint_SaleNotFound() {
        Long saleId = 1L;
        String email = "test@test.com";

        Grade grade = Grade.builder()
                .id(1L)
                .name("MEMBER")
                .pointRatio(10)
                .condition(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .grade(grade)
                .point(1000)
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class,
                () -> memberService.updateCancelSalePoint(saleId, email));

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(saleRepository, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("주문 취소시 적립금 회수 - 성공 (결제를 진행하지 않은 주문)")
    void testUpdateCancelSalePoint_Success_NotPaid() {
        Long saleId = 1L;
        String email = "test@test.com";

        Grade grade = Grade.builder()
                .id(1L)
                .name("MEMBER")
                .pointRatio(10)
                .condition(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .grade(grade)
                .point(1000)
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        Sale sale = Sale.builder()
                .saleId(saleId)
                .member(member)
                .salePaymentStatus(SalePaymentStatus.WAITING)
                .salePointUsage(1000)
                .saleTotalPrice(10000)
                .build();

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        memberService.updateCancelSalePoint(saleId, email);

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(saleRepository, times(1)).findById(anyLong());
        verify(pointHistoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문 취소시 적립금 회수 - 성공 (결제를 진행한 주문)")
    void testUpdateCancelSalePoint_Success_Paid() {
        Long saleId = 1L;
        String email = "test@test.com";

        Grade grade = Grade.builder()
                .id(1L)
                .name("MEMBER")
                .pointRatio(10)
                .condition(0)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .grade(grade)
                .point(1000)
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        Sale sale = Sale.builder()
                .saleId(saleId)
                .member(member)
                .salePaymentStatus(SalePaymentStatus.PAID)
                .salePointUsage(1000)
                .saleTotalPrice(10000)
                .build();

        given(saleRepository.findById(anyLong()))
                .willReturn(Optional.of(sale));

        PointHistory pointHistory = PointHistory.builder()
                .pointHistoryId(1L)
                .pointHistoryReason("주문 적립")
                .pointHistoryPoint(1000)
                .build();

        given(pointHistoryRepository.findBySale_SaleId(anyLong()))
                .willReturn(Optional.of(pointHistory));

        memberService.updateCancelSalePoint(saleId, email);

        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(saleRepository, times(1)).findById(anyLong());
        verify(pointHistoryRepository, times(1)).findBySale_SaleId(anyLong());
        verify(pointHistoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원 마지막 로그인 일자 수정 - 실패 (존재하지 않는 회원)")
    void updateLatestLoginAt_MemberNotFound() {
        Long memberId = 1L;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.updateLatestLoginAt(memberId));

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 마지막 로그인 일자 수정 - 성공")
    void updateLatestLoginAt_Success() {
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@test.com")
                .point(1000)
                .latestLoginAt(null)
                .build();


        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        memberService.updateLatestLoginAt(memberId);

        assertNotNull(member.getLatestLoginAt());
        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 상태 변경 - 실패 (존재하지 않는 회원)")
    void testChangeState_Fail_MemberNotFound() {
        Long memberId = 1L;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.changeState(memberId, Member.State.DORMANT));

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 상태 변경 - 실패 (동일한 상태로 변경한 경우)")
    void testChangeState_Fail_SameState() {
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@test.com")
                .state(Member.State.ACTIVE)
                .build();

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        assertThrows(MemberCannotChangeStateException.class,
                () -> memberService.changeState(memberId, Member.State.ACTIVE));
    }

    @Test
    @DisplayName("회원 상태 변경 - 성공")
    void testChangeState_Success() {
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@test.com")
                .state(Member.State.ACTIVE)
                .build();

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        memberService.changeState(memberId, Member.State.DORMANT);

        assertEquals(Member.State.DORMANT, member.getState());
    }

    @Test
    @DisplayName("회원 ID로 비밀번호 조회 - 실패 (존재하지 않는 회원)")
    void testGetPassword_Fail_MemberNotFound() {
        Long memberId = 1L;

        assertThrows(MemberNotFoundException.class,
                () -> memberService.getPassword(memberId));

        verify(memberRepository, times(1)).getPassword(anyLong());
    }

    @Test
    @DisplayName("회원 ID로 비밀번호 조회 - 성공")
    void testGetPassword_Success() {
        Long memberId = 1L;


        MemberPasswordResponseDto memberPassword = new MemberPasswordResponseDto("1234");

        given(memberRepository.getPassword(anyLong()))
                .willReturn(Optional.of(memberPassword));

        MemberPasswordResponseDto actual = memberService.getPassword(memberId);

        assertEquals(memberPassword.getPassword(), actual.getPassword());
        verify(memberRepository, times(1)).getPassword(anyLong());
    }

    @Test
    @DisplayName("회원 비밀번호 수정 - 실패 (존재하지 않는 회원)")
    void testChangePassword_Fail_MemberNotFound() {
        Long memberId = 1L;

        MemberPasswordRequestDto memberPassword = new MemberPasswordRequestDto();
        ReflectionTestUtils.setField(memberPassword, "password", "1234");

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.changePassword(memberId, memberPassword));

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 비밀번호 수정 - 실패 (동일한 비밀번호로 변경한 경우)")
    void testChangePassword_Fail_SamePassword() {
        Long memberId = 1L;

        Member member = Member.builder()
                .email("test@test.com")
                .password("1234")
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        MemberPasswordRequestDto memberPassword = new MemberPasswordRequestDto();
        ReflectionTestUtils.setField(memberPassword, "password", "1234");

        assertThrows(MemberPasswordCannotChangeException.class,
                () -> memberService.changePassword(memberId, memberPassword));

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 비밀번호 수정 - 성공")
    void testChangePassword_Success() {
        Long memberId = 1L;

        Member member = Member.builder()
                .email("test@test.com")
                .password("1234")
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        MemberPasswordRequestDto memberPassword = new MemberPasswordRequestDto();
        ReflectionTestUtils.setField(memberPassword, "password", "test1234!!");

        memberService.changePassword(memberId, memberPassword);

        verify(memberRepository, times(1)).findById(anyLong());
        assertEquals(member.getPassword(), memberPassword.getPassword());
    }

    @Test
    @DisplayName("회원 정보 수정 - 실패 (존재하지 않는 회원)")
    void testUpdateMemberInfo_Fail_MemberNotFound() {
        Long memberId = 1L;

        MemberUpdateRequestDto memberDto = new MemberUpdateRequestDto();
        ReflectionTestUtils.setField(memberDto, "name", "테스터");
        ReflectionTestUtils.setField(memberDto, "contact", "01012341234");
        ReflectionTestUtils.setField(memberDto, "birth", LocalDate.of(1999, 5, 13));

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.updateMemberInfo(memberId, memberDto));
    }

    @Test
    @DisplayName("회원 정보 수정 - 성공")
    void testUpdateMemberInfo_Success() {
        Long memberId = 1L;

        MemberUpdateRequestDto memberDto = new MemberUpdateRequestDto();
        ReflectionTestUtils.setField(memberDto, "name", "테스터");
        ReflectionTestUtils.setField(memberDto, "contact", "01012341234");
        ReflectionTestUtils.setField(memberDto, "birth", LocalDate.of(1999, 5, 13));

        Member member = Member.builder()
                .name("CKIN")
                .contact("01011112222")
                .birth(LocalDate.of(2002, 1, 1))
                .build();

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        memberService.updateMemberInfo(memberId, memberDto);

        assertAll(
                () -> assertEquals(memberDto.getName(), member.getName()),
                () -> assertEquals(memberDto.getContact(), member.getContact()),
                () -> assertEquals(memberDto.getBirth(), member.getBirth())
        );

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("회원 상세 정보 조회 - 실패 (존재하지 않는 회원)")
    void testGetMemberDetailInfo_Fail_MemberNotFound() {
        Long memberId = 1L;

        given(memberRepository.getMemberDetailInfo(memberId))
                .willReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> memberService.getMemberDetailInfo(memberId));

        verify(memberRepository, times(1)).getMemberDetailInfo(anyLong());
    }

    @Test
    @DisplayName("회원 상세 정보 조회 - 성공")
    void testGetMemberDetailInfo_Success() {
        Long memberId = 1L;

        MemberDetailInfoResponseDto member
                = new MemberDetailInfoResponseDto("테스터", "01012341234", LocalDate.of(1999, 5, 13));

        given(memberRepository.getMemberDetailInfo(anyLong()))
                .willReturn(Optional.of(member));

        MemberDetailInfoResponseDto actual = memberService.getMemberDetailInfo(memberId);

        assertAll(
                () -> assertEquals(member.getName(), actual.getName()),
                () -> assertEquals(member.getContact(), actual.getContact()),
                () -> assertEquals(member.getBirth(), actual.getBirth())
        );
    }
}
