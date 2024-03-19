package store.ckin.api.address.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.ckin.api.address.domain.request.AddressAddRequestDto;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.address.domain.response.MemberAddressResponseDto;
import store.ckin.api.address.exception.AddressAlreadyExistsException;
import store.ckin.api.address.exception.AddressNotFoundException;
import store.ckin.api.address.service.AddressService;
import store.ckin.api.member.exception.MemberNotFoundException;

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
                                    @Valid @RequestBody AddressAddRequestDto addressAddRequestDto) {
        addressService.addAddress(memberId, addressAddRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/members/{memberId}/address")
    ResponseEntity<List<MemberAddressResponseDto>> getMemberAddressList(@PathVariable("memberId") Long memberId) {
        List<MemberAddressResponseDto> addressList = addressService.getMemberAddressList(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @PutMapping("/members/{memberId}/addresses/{addressId}")
    ResponseEntity<Void> updateAddress(@PathVariable("memberId") Long memberId,
                                       @PathVariable("addressId") Long addressId,
                                       @Valid @RequestBody AddressUpdateRequestDto addressUpdateRequestDto) {
        addressService.updateAddress(memberId, addressId, addressUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/members/{memberId}/addresses/{addressId}/default")
    ResponseEntity<Void> setDefaultAddress(@PathVariable("memberId") Long memberId,
                                       @PathVariable("addressId") Long addressId) {
        addressService.setDefaultAddress(memberId, addressId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/members/{memberId}/addresses/{addressId}")
    ResponseEntity<Void> deleteAddress(@PathVariable("memberId") Long memberId,
                                       @PathVariable("addressId") Long addressId) {
        addressService.deleteAddress(memberId, addressId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler({ MemberNotFoundException.class, AddressNotFoundException.class })
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AddressAlreadyExistsException.class)
    public ResponseEntity<Void> handleAlreadyExistsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
