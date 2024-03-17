package store.ckin.api.address.service;

import java.util.List;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;

/**
 * Address 에 관한 로직을 처리하는 서비스 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
public interface AddressService {
    boolean isDefaultAddress(Long addressId);

    void addAddress(Long memberId, AddressAddRequestDto addressAddRequestDto);

    List<MemberAddressResponseDto> getMemberAddressList(Long memberId);

    void updateAddress(Long memberId, AddressUpdateRequestDto addressUpdateRequestDto);

    void setDefaultAddress(Long addressId);

    void deleteAddress(Long addressId);
}
