package store.ckin.api.address.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.service.AddressService;

/**
 * Address 에 관한 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 18.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/members/{memberId}/address")
    ResponseEntity<Void> addAddress(@PathVariable("memberId") Long memberId,
                                    AddressAddRequestDto addressAddRequestDto) {
        addressService.addAddress(memberId, addressAddRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/members/{memberId}/address")
    ResponseEntity<List<MemberAddressResponseDto>> getMemberAddressList(@PathVariable("memberId") Long memberId) {
        List<MemberAddressResponseDto> addressList = addressService.getMemberAddressList(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @PostMapping("/members/{memberId}/addresses/{addressId}")
    ResponseEntity<Void> updateAddress(@PathVariable("memberId") Long memberId,
                                       @PathVariable("addressId") Long addressId,
                                       AddressUpdateRequestDto addressUpdateRequestDto) {
        addressService.updateAddress(memberId, addressId, addressUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
