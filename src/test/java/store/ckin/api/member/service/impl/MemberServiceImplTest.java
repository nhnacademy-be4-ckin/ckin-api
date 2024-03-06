package store.ckin.api.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
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
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.repository.MemberRepository;

/**
 * MemberService 에 대한 Test 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("멤버 생성 성공 테스트")
    void testCreateMemberSuccess() {
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
}