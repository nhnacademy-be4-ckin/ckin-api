package store.ckin.api.address.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.address.domain.exception.AddressNotFoundException;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.repository.AddressRepository;
import store.ckin.api.address.service.AddressService;

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

    @Transactional(readOnly = true)
    @Override
    public boolean isDefaultAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException();
        }

        return addressRepository.isDefaultAddress(addressId);
    }

    @Override
    public void addAddress(Long memberId, AddressAddRequestDto addressAddRequestDto) {

    }

    @Override
    public List<MemberAddressResponseDto> getMemberAddressList(Long memberId) {
        return null;
    }

    @Override
    public void updateAddress(Long memberId, AddressUpdateRequestDto addressUpdateRequestDto) {

    }

    @Override
    public void setDefaultAddress(Long addressId) {

    }

    @Override
    public void deleteAddress(Long addressId) {

    }
}
