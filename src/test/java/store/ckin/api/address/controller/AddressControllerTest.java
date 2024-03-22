package store.ckin.api.address.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
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
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.exception.AddressAlreadyExistsException;
import store.ckin.api.address.exception.AddressNotFoundException;
import store.ckin.api.address.service.AddressService;
import store.ckin.api.member.exception.MemberNotFoundException;

/**
 * AddressController 테스트 코드 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 22.
 */
@WebMvcTest(AddressController.class)
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("주소 생성")
    void testAddAddress() throws Exception {
        AddressAddRequestDto dto = new AddressAddRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "123456");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        doNothing().when(addressService).addAddress(1L, dto);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/members/{memberId}/address", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andDo(document("address/addAddress/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberId").description("멤버 ID")),
                        requestFields(
                                fieldWithPath("postCode").description("우편번호"),
                                fieldWithPath("base").description("기본주소"),
                                fieldWithPath("detail").description("상세주소"),
                                fieldWithPath("alias").description("별칭")
                        )
                ));

        verify(addressService).addAddress(anyLong(), any());
    }

    @Test
    @DisplayName("주소 생성 실패 : 이미 등록된 정보의 주소")
    void testAddAddressFailedAlreadyExistsAddress() throws Exception {
        AddressAddRequestDto dto = new AddressAddRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "123456");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        doThrow(AddressAlreadyExistsException.class)
                .when(addressService).addAddress(anyLong(), any(AddressAddRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/members/{memberId}/address", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(document("address/addAddress/conflict",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).addAddress(anyLong(), any());
    }

    @Test
    @DisplayName("멤버별 주소 조회")
    void testGetMemberAddressList() throws Exception {
        MemberAddressResponseDto address = new MemberAddressResponseDto(
                1L,
                "123456",
                "광주 광역시",
                "광산구 월계로",
                "우리집",
                false
        );

        MemberAddressResponseDto address2 = new MemberAddressResponseDto(
                1L,
                "427891",
                "광주 광역시",
                "조선대학교",
                "학교",
                false
        );

        when(addressService.getMemberAddressList(1L))
                .thenReturn(List.of(address, address2));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/members/{memberId}/address", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.[0].addressId").value(address.getAddressId()),
                        jsonPath("$.[0].postCode").value(address.getPostCode()),
                        jsonPath("$.[0].base").value(address.getBase()),
                        jsonPath("$.[0].detail").value(address.getDetail()),
                        jsonPath("$.[0].alias").value(address.getAlias()),
                        jsonPath("$.[0].isDefault").value(address.getIsDefault()))
                .andDo(document("address/getMemberAddressList/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("memberId").description("멤버 ID")),
                        responseFields(
                                fieldWithPath("[].addressId").description("주소 ID"),
                                fieldWithPath("[].postCode").description("우편 번호"),
                                fieldWithPath("[].base").description("기본 주소"),
                                fieldWithPath("[].detail").description("상세 주소"),
                                fieldWithPath("[].alias").description("별칭"),
                                fieldWithPath("[].isDefault").description("계정 기본 주소 설정 여부")
                        )
                ));

        verify(addressService).getMemberAddressList(1L);
    }

    @Test
    @DisplayName("존재하지 않는 멤버 ID로 조회 시, 멤버별 주소 조회 실패")
    void testGetMemberAddressListFailedNotFoundMember() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(addressService)
                .getMemberAddressList(1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/members/{memberId}/address", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("address/getMemberAddressList/not-found-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).getMemberAddressList(1L);
    }

    @Test
    @DisplayName("존재하지 않는 주소 ID로 조회 시, 멤버별 주소 조회 실패")
    void testGetMemberAddressListFailedNotFoundAddress() throws Exception {
        doThrow(AddressNotFoundException.class)
                .when(addressService)
                .getMemberAddressList(1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/members/{memberId}/address", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("address/getMemberAddressList/not-found-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).getMemberAddressList(1L);
    }

    @Test
    @DisplayName("주소 업데이트")
    void testUpdateAddress() throws Exception {
        AddressUpdateRequestDto dto = new AddressUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "123456");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        doNothing().when(addressService).updateAddress(1L, 1L, dto);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(document("address/updateAddress/not-found",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID"),
                                parameterWithName("addressId").description("주소 ID")),
                        requestFields(
                                fieldWithPath("postCode").description("우편번호"),
                                fieldWithPath("base").description("기본주소"),
                                fieldWithPath("detail").description("상세주소"),
                                fieldWithPath("alias").description("별칭")
                        )
                ));

        verify(addressService).updateAddress(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("없는 멤버 ID로 요청 시, 주소 업데이트 실패")
    void testUpdateAddressNotFoundMember() throws Exception {
        AddressUpdateRequestDto dto = new AddressUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "123456");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        doThrow(MemberNotFoundException.class)
                .when(addressService)
                .updateAddress(anyLong(), anyLong(), any(AddressUpdateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(document("address/updateAddress/not-found-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).updateAddress(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("없는 주소 ID로 요청 시, 주소 업데이트 실패")
    void testUpdateAddressNotFoundAddress() throws Exception {
        AddressUpdateRequestDto dto = new AddressUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "123456");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        doThrow(AddressNotFoundException.class)
                .when(addressService)
                .updateAddress(anyLong(), anyLong(), any(AddressUpdateRequestDto.class));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(document("address/updateAddress/not-found-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).updateAddress(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("기본 주소지로 설정")
    void testSetDefaultAddress() throws Exception {
        doNothing().when(addressService).setDefaultAddress(1L, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/addresses/{addressId}/default", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("address/setDefaultAddress/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID"),
                                parameterWithName("addressId").description("주소 ID"))
                ));

        verify(addressService).setDefaultAddress(anyLong(), anyLong());
    }

    @Test
    @DisplayName("없는 멤버 ID로 요청 시, 기본 주소지로 설정 실패")
    void testSetDefaultAddressFailedNotFoundMember() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(addressService)
                .setDefaultAddress(1L, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/addresses/{addressId}/default", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("address/setDefaultAddress/not-found-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).setDefaultAddress(anyLong(), anyLong());
    }

    @Test
    @DisplayName("없는 주소 ID로 요청 시, 기본 주소지로 설정 실패")
    void testSetDefaultAddressFailedNotFoundAddress() throws Exception {
        doThrow(AddressNotFoundException.class)
                .when(addressService)
                .setDefaultAddress(1L, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .put("/api/members/{memberId}/addresses/{addressId}/default", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("address/setDefaultAddress/not-found-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).setDefaultAddress(anyLong(), anyLong());
    }

    @Test
    @DisplayName("주소 삭제")
    void testDeleteAddress() throws Exception {
        doNothing().when(addressService).deleteAddress(1L, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("address/deleteAddress/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID"),
                                parameterWithName("addressId").description("주소 ID"))
                ));

        verify(addressService).deleteAddress(anyLong(), anyLong());
    }

    @Test
    @DisplayName("없는 멤버 ID 로 요청 시, 주소 삭제 실패")
    void testDeleteAddressFailedNotFoundMember() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(addressService)
                .deleteAddress(1L, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("address/deleteAddress/not-found-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).deleteAddress(anyLong(), anyLong());
    }

    @Test
    @DisplayName("없는 주소 ID 로 요청 시, 주소 삭제 실패")
    void testDeleteAddressFailedNotFoundAddress() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(addressService)
                .deleteAddress(1L, 1L);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete("/api/members/{memberId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("address/deleteAddress/not-found-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        verify(addressService).deleteAddress(anyLong(), anyLong());
    }
}