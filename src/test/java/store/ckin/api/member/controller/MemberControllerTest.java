package store.ckin.api.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.member.domain.MemberCreateRequestDto;
import store.ckin.api.member.service.MemberService;

/**
 * MemberController 에 대한 Test 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("멤버 생성 성공 테스트")
    void testCreateMemberSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        MemberCreateRequestDto dto = new MemberCreateRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        ReflectionTestUtils.setField(dto, "password", "password 123");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());

        doNothing().when(memberService).createMember(dto);

        mockMvc.perform(post("/api/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(memberService).createMember(any(MemberCreateRequestDto.class));
    }
}