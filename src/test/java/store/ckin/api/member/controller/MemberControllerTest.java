package store.ckin.api.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.member.domain.request.*;
import store.ckin.api.member.domain.response.*;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberCannotChangeStateException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.exception.MemberOauthNotFoundException;
import store.ckin.api.member.service.MemberService;
import store.ckin.api.pointpolicy.exception.PointPolicyNotFoundException;

/**
 * MemberController 에 대한 Test 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("멤버 생성 성공 테스트")
    void testCreateMemberSuccess() throws Exception {
        MemberCreateRequestDto dto = new MemberCreateRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        ReflectionTestUtils.setField(dto, "password", "password 123");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());
        ReflectionTestUtils.setField(dto, "oauthId", null);

        doNothing().when(memberService).createMember(dto);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andDo(document("member/createMember/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("계정이름"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("birth").description("생년월일"),
                                fieldWithPath("oauthId").description("소셜 로그인 플랫폼 ID"))
                        ));

        verify(memberService).createMember(any(MemberCreateRequestDto.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일일 경우 : 멤버 생성 실패")
    void testCreateMemberFailedAlreadyExistsEmail() throws Exception {
        MemberCreateRequestDto dto = new MemberCreateRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        ReflectionTestUtils.setField(dto, "password", "password 123");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());
        ReflectionTestUtils.setField(dto, "oauthId", null);

        doThrow(MemberAlreadyExistsException.class)
                .when(memberService).createMember(any(MemberCreateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andDo(document("member/createMember/conflict",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).createMember(any(MemberCreateRequestDto.class));
    }

    @Test
    @DisplayName("기본 설정 등급이 존재하지 않을 경우 멤버 생성 실패")
    void testCreateMemberFailedNotFoundGrade() throws Exception {
        MemberCreateRequestDto dto = new MemberCreateRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        ReflectionTestUtils.setField(dto, "password", "password 123");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());
        ReflectionTestUtils.setField(dto, "oauthId", null);

        doThrow(GradeNotFoundException.class)
                .when(memberService).createMember(any(MemberCreateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(document("member/createMember/not-found-grade",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).createMember(any(MemberCreateRequestDto.class));
    }

    @Test
    @DisplayName("기본 포인트 정책이 존재하지 않을 경우 멤버 생성 실패")
    void testCreateMemberFailedNotFoundPointPolicy() throws Exception {
        MemberCreateRequestDto dto = new MemberCreateRequestDto();
        ReflectionTestUtils.setField(dto, "email", "a@naver.com");
        ReflectionTestUtils.setField(dto, "password", "password 123");
        ReflectionTestUtils.setField(dto, "name", "abc");
        ReflectionTestUtils.setField(dto, "contact", "0101111234");
        ReflectionTestUtils.setField(dto, "birth", LocalDate.now());
        ReflectionTestUtils.setField(dto, "oauthId", null);

        doThrow(PointPolicyNotFoundException.class)
                .when(memberService).createMember(any(MemberCreateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(document("member/createMember/not-found-grade",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).createMember(any(MemberCreateRequestDto.class));
    }

    @Test
    @DisplayName("이메일 중복확인을 위해 조회")
    void testCheckDuplicateEmail() throws Exception {
        MemberEmailOnlyRequestDto dto = new MemberEmailOnlyRequestDto();

        ReflectionTestUtils.setField(dto, "email", "test@test.com");

        when(memberService.alreadyExistsEmail(dto))
                .thenReturn(anyBoolean());

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/checkEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("member/checkDuplicateEmail/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"))
                ));

        verify(memberService).alreadyExistsEmail(any(MemberEmailOnlyRequestDto.class));
    }

    @Test
    @DisplayName("비밀번호 확인을 위해 조회")
    void testCheckPasswordSuccess() throws Exception {
        MemberPasswordResponseDto dto = new MemberPasswordResponseDto("ckin1234");

        when(memberService.getPassword(anyLong())).thenReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/members/{memberId}/checkPassword", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value(dto.getPassword()))
                .andDo(document("member/checkPassword/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")),
                        responseFields(
                                fieldWithPath("password").description("비밀번호"))
                ));

        verify(memberService).getPassword(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 인한 비밀번호 확인을 위해 조회 실패")
    void testCheckPasswordFailed() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(memberService).getPassword(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/members/{memberId}/checkPassword", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("member/checkPassword/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).getPassword(anyLong());
    }

    @Test
    @DisplayName("로그인 시 필요한 정보를 조회")
    void testGetMemberInfo() throws Exception {
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto();

        ReflectionTestUtils.setField(requestDto, "email", "test@test.com");

        MemberAuthResponseDto responseDto = new MemberAuthResponseDto(
                1L,
                "test@test.com",
                "ckin1234",
                Member.Role.MEMBER
        );

        when(memberService.getLoginMemberInfo(any(MemberAuthRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(responseDto.getId()),
                        jsonPath("$.email").value(responseDto.getEmail()),
                        jsonPath("$.password").value(responseDto.getPassword()),
                        jsonPath("$.role").value(responseDto.getRole()))
                .andDo(document("member/getMemberInfo/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("id").description("멤버 ID"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("role").description("권한"))
                        ));

        verify(memberService).getLoginMemberInfo(any(MemberAuthRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 멤버 ID로 인한, 로그인 시 필요한 정보를 조회 살패")
    void testGetMemberInfoFailed() throws Exception {
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto();

        ReflectionTestUtils.setField(requestDto, "email", "test@test.com");
        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .getLoginMemberInfo(any(MemberAuthRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(document("member/getMemberInfo/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).getLoginMemberInfo(any(MemberAuthRequestDto.class));
    }

    @Test
    @DisplayName("소셜 로그인 플랫폼 ID로 로그인 관련 정보 조회")
    void testGetOauthMemberInfo() throws Exception {
        MemberOauthIdOnlyRequestDto requestDto = new MemberOauthIdOnlyRequestDto();

        ReflectionTestUtils.setField(requestDto, "oauthId", "oauth1234");

        MemberOauthLoginResponseDto responseDto =
                new MemberOauthLoginResponseDto(1L, Member.Role.MEMBER);

        when(memberService.getOauthMemberInfo(any(MemberOauthIdOnlyRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/api/login/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(responseDto.getId()),
                        jsonPath("$.role").value(responseDto.getRole()))
                .andDo(document("member/getOauthMemberInfo/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("oauthId").description("소셜 로그인 플랫폼 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("멤버 ID"),
                                fieldWithPath("role").description("멤버 권한"))
                ));

        verify(memberService).getOauthMemberInfo(any(MemberOauthIdOnlyRequestDto.class));
    }

    @Test
    @DisplayName("소셜 로그인 플랫폼 ID로 로그인 관련 정보 조회 실패")
    void testGetOauthMemberInfoFailed() throws Exception {
        MemberOauthIdOnlyRequestDto requestDto = new MemberOauthIdOnlyRequestDto();

        ReflectionTestUtils.setField(requestDto, "oauthId", "oauth1234");

        doThrow(MemberOauthNotFoundException.class).when(memberService)
                .getOauthMemberInfo(any(MemberOauthIdOnlyRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/login/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(document("member/getOauthMemberInfo/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).getOauthMemberInfo(any(MemberOauthIdOnlyRequestDto.class));
    }

    @Test
    @DisplayName("계정 접속시간 갱신")
    void testMemberUpdateLoginLog() throws Exception {
        doNothing().when(memberService).updateLatestLoginAt(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/api/members/{memberId}/update-log", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/memberUpdateLoginLog/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        )
                ));

        verify(memberService).updateLatestLoginAt(anyLong());
    }

    @Test
    @DisplayName("없는 멤버 ID 로 인한 계정 접속시간 갱신실패")
    void testMemberUpdateLoginLogFailed() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(memberService).updateLatestLoginAt(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/update-log", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("member/memberUpdateLoginLog/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).updateLatestLoginAt(anyLong());
    }

    @Test
    @DisplayName("계정 활성화")
    void testSetActiveMember() throws Exception {
        doNothing().when(memberService)
                .changeState(anyLong(), any(Member.State.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/api/members/{memberId}/active", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/setActiveMember/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        )
                ));

        verify(memberService).changeState(anyLong(), any(Member.State.class));
    }

    @Test
    @DisplayName("없는 멤버 ID 로 인한 계정 활성화 실패")
    void testSetActiveMemberFailed() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .changeState(anyLong(), any(Member.State.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/active", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("member/setActiveMember/not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).changeState(anyLong(), any(Member.State.class));
    }

    @Test
    @DisplayName("이미 변경하려는 상태일 경우, 계정 활성화 실패")
    void testSetActiveMemberFailedCannotChangeState() throws Exception {
        doThrow(MemberCannotChangeStateException.class)
                .when(memberService)
                .changeState(anyLong(), any(Member.State.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/active", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(document("member/setActiveMember/conflict",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).changeState(anyLong(), any(Member.State.class));
    }

    @Test
    @DisplayName("계정을 휴면으로 전환")
    void testSetDormantMember() throws Exception {
        doNothing().when(memberService)
                .changeState(anyLong(), any(Member.State.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/dormant", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/setDormantMember/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        )
                ));

        verify(memberService).changeState(anyLong(), any(Member.State.class));
    }

    @Test
    @DisplayName("없는 멤버 ID 로 인한 계정 활성화 실패")
    void testSetDormantMemberFailed() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .changeState(anyLong(), any(Member.State.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/dormant", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("member/setDormantMember/not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).changeState(anyLong(), any(Member.State.class));
    }

    @Test
    @DisplayName("이미 변경하려는 상태일 경우, 계정 활성화 실패")
    void testSetDormantMemberFailedCannotChangeState() throws Exception {
        doThrow(MemberCannotChangeStateException.class)
                .when(memberService)
                .changeState(anyLong(), any(Member.State.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/dormant", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(document("member/setDormantMember/conflict",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).changeState(anyLong(), any(Member.State.class));
    }

    @Test
    @DisplayName("비밀번호 변경")
    void setChangePassword() throws Exception {
        MemberPasswordRequestDto requestDto = new MemberPasswordRequestDto();

        ReflectionTestUtils.setField(requestDto, "password", "ckin1234");

        doNothing().when(memberService)
                .changePassword(anyLong(), any(MemberPasswordRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/api/members/{memberId}/password", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("member/changePassword/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").description("변경할 비밀번호")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID"))
                ));

        verify(memberService).changePassword(anyLong(), any(MemberPasswordRequestDto.class));
    }

    @Test
    @DisplayName("비밀번호 변경 실패")
    void setChangePasswordFailed() throws Exception {
        MemberPasswordRequestDto requestDto = new MemberPasswordRequestDto();

        ReflectionTestUtils.setField(requestDto, "password", "ckin1234");

        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .changePassword(anyLong(), any(MemberPasswordRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/password", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(document("member/changePassword/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).changePassword(anyLong(), any(MemberPasswordRequestDto.class));
    }

    @Test
    @DisplayName("멤버 정보 수정")
    void testUpdateMemberInfo() throws Exception {
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto();

        ReflectionTestUtils.setField(requestDto, "name", "tester");
        ReflectionTestUtils.setField(requestDto, "contact", "01012341234");
        ReflectionTestUtils.setField(requestDto, "birth", LocalDate.now());

        doNothing().when(memberService)
                .updateMemberInfo(anyLong(), any(MemberUpdateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/api/members/{memberId}/update-info", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("member/updateMemberInfo/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("birth").description("생일"))
                ));

        verify(memberService).updateMemberInfo(anyLong(), any(MemberUpdateRequestDto.class));
    }

    @Test
    @DisplayName("멤버 정보 수정 실패")
    void testUpdateMemberInfoFailed() throws Exception {
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto();

        ReflectionTestUtils.setField(requestDto, "name", "tester");
        ReflectionTestUtils.setField(requestDto, "contact", "01012341234");
        ReflectionTestUtils.setField(requestDto, "birth", LocalDate.now());

        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .updateMemberInfo(anyLong(), any(MemberUpdateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/update-info", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(document("member/updateMemberInfo/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).updateMemberInfo(anyLong(), any(MemberUpdateRequestDto.class));
    }

    @Test
    @DisplayName("멤버 수정 폼으로 기존 멤버 정보 조회")
    void testGetMemberDetailInfo() throws Exception {
        MemberDetailInfoResponseDto responseDto =
                new MemberDetailInfoResponseDto(
                        "tester",
                        "01023412341",
                        LocalDate.now()
                );

        when(memberService.getMemberDetailInfo(anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/members/{memberId}/info", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.name").value(responseDto.getName()),
                        jsonPath("$.contact").value(responseDto.getContact()),
                        jsonPath("$.birth").value(responseDto.getBirth().toString())
                )
                .andDo(document("member/getMemberDetailInfo/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("birth").description("생일"))
                ));

        verify(memberService).getMemberDetailInfo(anyLong());
    }

    @Test
    @DisplayName("멤버 수정 폼으로 기존 멤버 정보 조회 실패")
    void testGetMemberDetailInfoFailed() throws Exception {
        MemberDetailInfoResponseDto responseDto =
                new MemberDetailInfoResponseDto(
                        "tester",
                        "01023412341",
                        LocalDate.now()
                );

        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .getMemberDetailInfo(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/members/{memberId}/info", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("member/getMemberDetailInfo/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).getMemberDetailInfo(anyLong());
    }

    @Test
    @DisplayName("마이페이지 헤더에 들어가는 정보 조회")
    void testGetMyPageInfo() throws Exception {
        MemberMyPageResponseDto responseDto = new MemberMyPageResponseDto(
                "tester",
                "일반",
                0,
                5000,
                100000,
                0L
        );

        when(memberService.getMyPageInfo(anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/api/members/mypage/{memberId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.name").value(responseDto.getName()),
                        jsonPath("$.gradeName").value(responseDto.getGradeName()),
                        jsonPath("$.accumulateAmount").value(responseDto.getAccumulateAmount()),
                        jsonPath("$.point").value(responseDto.getPoint()),
                        jsonPath("$.gradeCondition").value(responseDto.getGradeCondition()),
                        jsonPath("$.countReview").value(responseDto.getCountReview())
                )
                .andDo(document("member/getMyPageInfo/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("gradeName").description("등급 이름"),
                                fieldWithPath("accumulateAmount").description("누적 구매 금액"),
                                fieldWithPath("point").description("포인트"),
                                fieldWithPath("gradeCondition").description("등급 조건"),
                                fieldWithPath("countReview").description("등록한 리뷰 수"))
                ));

        verify(memberService).getMyPageInfo(anyLong());
    }

    @Test
    @DisplayName("마이페이지 헤더에 들어가는 정보 조회 실패")
    void testGetMyPageInfoFailed() throws Exception {
        MemberMyPageResponseDto responseDto = new MemberMyPageResponseDto(
                "tester",
                "일반",
                0,
                5000,
                100000,
                0L
        );

        doThrow(MemberNotFoundException.class)
                .when(memberService)
                .getMyPageInfo(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/members/mypage/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("member/getMyPageInfo/failed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(memberService).getMyPageInfo(anyLong());
    }
}