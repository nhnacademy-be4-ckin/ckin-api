package store.ckin.api.address.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.entity.Address;
import store.ckin.api.address.exception.AddressAlreadyExistsException;
import store.ckin.api.address.exception.AddressNotFoundException;
import store.ckin.api.address.repository.AddressRepository;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;

/**
 * AddressService Test 코드 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 22.
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        Grade grade = Grade.builder()
                .id(1L)
                .name("일반")
                .pointRatio(10)
                .condition(0)
                .build();

        member = Member.builder()
                .id(1L)
                .grade(grade)
                .email("test@nhn.com")
                .password("1234")
                .name("tester")
                .contact("01012341234")
                .birth(LocalDate.of(2024, 1, 1))
                .state(Member.State.ACTIVE)
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .accumulateAmount(0)
                .build();
    }

    @Test
    @DisplayName("주소 생성 성공")
    void testAddAddressSuccess() {
        AddressAddRequestDto dto = new AddressAddRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "111111");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        when(memberRepository.findById(member.getId()))
                .thenReturn(Optional.of(member));
        when(addressRepository.existsByMemberIdAndBaseAndDetail(member.getId(), dto.getBase(), dto.getDetail()))
                .thenReturn(false);

        addressService.addAddress(member.getId(), dto);

        verify(memberRepository).findById(member.getId());
        verify(addressRepository)
                .existsByMemberIdAndBaseAndDetail(member.getId(), dto.getBase(), dto.getDetail());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("존재하지 않는 멤버로 인한 주소 생성 실패")
    void testAddAddressFailedNotFoundMember() {
        AddressAddRequestDto dto = new AddressAddRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "111111");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        when(memberRepository.findById(member.getId()))
                .thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class,
                () -> addressService.addAddress(member.getId(), dto));

        verify(memberRepository).findById(member.getId());
        verify(addressRepository, never())
                .existsByMemberIdAndBaseAndDetail(anyLong(), anyString(), anyString());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    @DisplayName("중복된 내용의 주소 존재로 인한 주소 생성 실패")
    void testAddAddressFailedAddressAlreadyExists() {
        AddressAddRequestDto dto = new AddressAddRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "111111");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        when(memberRepository.findById(member.getId()))
                .thenReturn(Optional.of(member));
        when(addressRepository.existsByMemberIdAndBaseAndDetail(member.getId(), dto.getBase(), dto.getDetail()))
                .thenReturn(true);

        assertThrows(AddressAlreadyExistsException.class,
                () -> addressService.addAddress(member.getId(), dto));

        verify(memberRepository).findById(member.getId());
        verify(addressRepository)
                .existsByMemberIdAndBaseAndDetail(member.getId(), dto.getBase(), dto.getDetail());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    @DisplayName("멤버 ID로 저장된 주소 리스트 조회")
    void testGetMemberAddressListSuccess() {
        MemberAddressResponseDto dto = new MemberAddressResponseDto(
                1L,
                "111111",
                "광주",
                "광역시",
                "광산구",
                false
        );

        when(memberRepository.existsById(member.getId()))
                .thenReturn(true);

        when(addressRepository.getMemberAddressList(member.getId()))
                .thenReturn(List.of(dto));

        List<MemberAddressResponseDto> result =
                addressService.getMemberAddressList(member.getId());

        assertEquals(1, result.size());

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository).getMemberAddressList(member.getId());
    }

    @Test
    @DisplayName("멤버 ID로 저장된 주소 리스트 조회 실패")
    void testGetMemberAddressListFailed() {
        when(memberRepository.existsById(member.getId()))
                .thenReturn(false);

        assertThrows(MemberNotFoundException.class,
                () -> addressService.getMemberAddressList(member.getId()));

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository, never()).getMemberAddressList(member.getId());
    }

    @Test
    @DisplayName("주소 업데이트 성공")
    void testUpdateAddressSuccess() {
        Address address = Address.builder()
                .id(1L)
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .isDefault(false)
                .build();

        AddressUpdateRequestDto dto = new AddressUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "111111");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        when(memberRepository.existsById(member.getId()))
                .thenReturn(true);
        when(addressRepository.findByIdAndMember_Id(1L, member.getId()))
                .thenReturn(Optional.of(address));

        addressService.updateAddress(1L, member.getId(), dto);

        assertEquals("111111", address.getPostCode());
        assertEquals("우리집", address.getAlias());

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository).findByIdAndMember_Id(1L, member.getId());
    }

    @Test
    @DisplayName("존재하지 않는 멤버로 인한 주소 업데이트 실패")
    void testUpdateAddressFailedNotFoundMember() {
        AddressUpdateRequestDto dto = new AddressUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "111111");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        when(memberRepository.existsById(member.getId()))
                .thenReturn(false);

        assertThrows(MemberNotFoundException.class,
                () -> addressService.updateAddress(1L, member.getId(), dto));

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository, never()).findByIdAndMember_Id(1L, member.getId());
    }

    @Test
    @DisplayName("주소 ID 와 멤버 ID 일치하는 주소가 없어서 실패")
    void testUpdateAddressFailedNotFoundAddress() {
        AddressUpdateRequestDto dto = new AddressUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "postCode", "111111");
        ReflectionTestUtils.setField(dto, "base", "광주");
        ReflectionTestUtils.setField(dto, "detail", "광역시");
        ReflectionTestUtils.setField(dto, "alias", "우리집");

        when(memberRepository.existsById(member.getId()))
                .thenReturn(true);
        when(addressRepository.findByIdAndMember_Id(1L, member.getId()))
                .thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class,
                () -> addressService.updateAddress(1L, member.getId(), dto));


        verify(memberRepository).existsById(member.getId());
        verify(addressRepository).findByIdAndMember_Id(1L, member.getId());
    }

    @Test
    @DisplayName("기본 주소로 변경 성공")
    void testSetDefaultAddressSuccess() {
        Address address = Address.builder()
                .id(1L)
                .member(member)
                .postCode("12345")
                .base("광주")
                .detail("광역시")
                .alias("광산구")
                .isDefault(false)
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .member(member)
                .postCode("323232")
                .base("테스트")
                .detail("광역시")
                .alias("광산구")
                .isDefault(true)
                .build();

        when(memberRepository.existsById(member.getId()))
                .thenReturn(true);
        when(addressRepository.findByIdAndMember_Id(1L, member.getId()))
                .thenReturn(Optional.of(address));
        when(addressRepository.findDefaultAddressByMemberId(member.getId()))
                .thenReturn(Optional.of(address2));

        addressService.setDefaultAddress(member.getId(), 1L);

        assertEquals(true, address.getIsDefault());
        assertEquals(false, address2.getIsDefault());

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository).findByIdAndMember_Id(1L, member.getId());
        verify(addressRepository).findDefaultAddressByMemberId(member.getId());
    }

    @Test
    @DisplayName("존재하지 않는 멤버로 인한 기본 주소로 변경 실패")
    void testSetDefaultAddressFailedNotFoundMember() {
        when(memberRepository.existsById(member.getId()))
                .thenReturn(false);

        assertThrows(MemberNotFoundException.class,
                () -> addressService.setDefaultAddress(member.getId(), 1L));

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository, never()).findByIdAndMember_Id(anyLong(), anyLong());
        verify(addressRepository, never()).findDefaultAddressByMemberId(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 주소로 인한 기본 주소로 변경 실패")
    void testSetDefaultAddressFailedNotFounAddress() {
        when(memberRepository.existsById(member.getId()))
                .thenReturn(true);
        when(addressRepository.findByIdAndMember_Id(1L, member.getId()))
                .thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class,
                () -> addressService.setDefaultAddress(member.getId(), 1L));

        verify(memberRepository).existsById(member.getId());
        verify(addressRepository).findByIdAndMember_Id(1L, member.getId());
        verify(addressRepository, never()).findDefaultAddressByMemberId(anyLong());
    }

    @Test
    @DisplayName("주소 삭제 성공")
    void testDeleteAddressSuccess() {
        when(memberRepository.existsById(1L))
                .thenReturn(true);
        when(addressRepository.existsByIdAndMember_Id(1L, 1L))
                .thenReturn(true);

        addressService.deleteAddress(1L, 1L);

        verify(memberRepository).existsById(1L);
        verify(addressRepository).existsByIdAndMember_Id(1L, 1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test
    @DisplayName("없는 멤버로 인한 주소 삭제 실패")
    void testDeleteAddressFailed() {
        when(memberRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(MemberNotFoundException.class,
                () -> addressService.deleteAddress(1L, 1L));

        verify(memberRepository).existsById(1L);
        verify(addressRepository, never()).existsByIdAndMember_Id(anyLong(), anyLong());
        verify(addressRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("없는 주소로 인한 주소 삭제 실패")
    void testDeleteAddressFailedNotFoundAddress() {
        when(memberRepository.existsById(1L))
                .thenReturn(true);
        when(addressRepository.existsByIdAndMember_Id(1L, 1L))
                .thenReturn(false);

        assertThrows(AddressNotFoundException.class,
                () -> addressService.deleteAddress(1L, 1L));

        verify(memberRepository).existsById(1L);
        verify(addressRepository).existsByIdAndMember_Id(1L, 1L);
        verify(addressRepository, never()).deleteById(anyLong());
    }

}