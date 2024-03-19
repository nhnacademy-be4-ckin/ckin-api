package store.ckin.api.address.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.entity.Address;
import store.ckin.api.address.exception.AddressAlreadyExistsException;
import store.ckin.api.address.exception.AddressNotFoundException;
import store.ckin.api.address.repository.AddressRepository;
import store.ckin.api.address.service.AddressService;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;

/**
 * AddressService 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void addAddress(Long memberId,
                           AddressAddRequestDto addressAddRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if (addressRepository.existsByMemberIdAndBaseAndDetail(
                memberId,
                addressAddRequestDto.getBase(),
                addressAddRequestDto.getDetail())) {
            throw new AddressAlreadyExistsException();
        }

        Address address = Address.builder()
                .member(member)
                .postCode(addressAddRequestDto.getPostCode())
                .base(addressAddRequestDto.getBase())
                .detail(addressAddRequestDto.getDetail())
                .alias(addressAddRequestDto.getAlias())
                .build();

        addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberAddressResponseDto> getMemberAddressList(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        return addressRepository.getMemberAddressList(memberId);
    }

    @Transactional
    @Override
    public void updateAddress(Long memberId,
                              Long addressId,
                              AddressUpdateRequestDto addressUpdateRequestDto) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        if (addressRepository.existsByMemberIdAndBaseAndDetail(
                memberId,
                addressUpdateRequestDto.getBase(),
                addressUpdateRequestDto.getDetail())) {
            throw new AddressAlreadyExistsException();
        }

        Address address = addressRepository.findByIdAndMember_Id(addressId, memberId)
                .orElseThrow(AddressNotFoundException::new);

        address.update(addressUpdateRequestDto);
    }

    @Transactional
    @Override
    public void setDefaultAddress(Long memberId, Long addressId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        Address address = addressRepository.findByIdAndMember_Id(addressId, memberId)
                .orElseThrow(AddressNotFoundException::new);

        if (!address.getIsDefault()) {
            addressRepository.findDefaultAddressByMemberId(memberId)
                            .ifPresent(Address::toggleDefault);

            address.toggleDefault();
        }
    }

    @Transactional
    @Override
    public void deleteAddress(Long memberId, Long addressId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        if (!addressRepository.existsByIdAndMember_Id(addressId, memberId)) {
            throw new AddressNotFoundException();
        }

        addressRepository.deleteById(addressId);
    }
}
