package store.ckin.api.address.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.address.domain.exception.AddressAlreadyExistsException;
import store.ckin.api.address.domain.exception.AddressNotFoundException;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.entity.Address;
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

    @Transactional(readOnly = true)
    @Override
    public boolean isDefaultAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException();
        }

        return addressRepository.isDefaultAddress(addressId);
    }

    @Transactional
    @Override
    public void addAddress(Long memberId, AddressAddRequestDto addressAddRequestDto) {
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
                .base(addressAddRequestDto.getBase())
                .base(addressAddRequestDto.getDetail())
                .base(addressAddRequestDto.getAlias())
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
    public void updateAddress(Long memberId, Long addressId, AddressUpdateRequestDto addressUpdateRequestDto) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(AddressNotFoundException::new);

        address.update(addressUpdateRequestDto);
    }

    @Override
    public void setDefaultAddress(Long addressId) {

    }

    @Override
    public void deleteAddress(Long addressId) {

    }
}
