package store.ckin.api.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
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

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("멤버 생성 성공 테스트")
    void testCreateMemberSuccess() {
        String testEmail = "test@test.com";

        when(memberRepository.existsByEmail(anyString()))
                .thenReturn(false);

        MemberCreateRequestDto dto = new MemberCreateRequestDto();

        ReflectionTestUtils.setField(dto, "email", testEmail);
        ReflectionTestUtils.setField(dto, "password", "pwd");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDateTime.now());

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
        ReflectionTestUtils.setField(dto, "birth", LocalDateTime.now());

        assertThrows(MemberAlreadyExistsException.class,
                () -> memberService.createMember(dto));

        verify(memberRepository).existsByEmail(testEmail);
        verify(memberRepository, never()).save(any(Member.class));
    }
}