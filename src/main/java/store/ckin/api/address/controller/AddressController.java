package store.ckin.api.address.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/api/members/{memberId}")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<Void> addAddress(@PathVariable("memberId") Long memberId,
                                           @Valid @RequestBody AddressAddRequestDto addressAddRequestDto) {
        addressService.addAddress(memberId, addressAddRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/address")
    public ResponseEntity<List<MemberAddressResponseDto>> getMemberAddressList(
            @PathVariable("memberId") Long memberId) {
        List<MemberAddressResponseDto> addressList = addressService.getMemberAddressList(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<Void> updateAddress(@PathVariable("memberId") Long memberId,
                                              @PathVariable("addressId") Long addressId,
                                              @Valid @RequestBody AddressUpdateRequestDto addressUpdateRequestDto) {
        addressService.updateAddress(memberId, addressId, addressUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/addresses/{addressId}/default")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable("memberId") Long memberId,
                                                  @PathVariable("addressId") Long addressId) {
        addressService.setDefaultAddress(memberId, addressId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("memberId") Long memberId,
                                              @PathVariable("addressId") Long addressId) {
        addressService.deleteAddress(memberId, addressId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler({MemberNotFoundException.class, AddressNotFoundException.class})
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AddressAlreadyExistsException.class)
    public ResponseEntity<Void> handleAlreadyExistsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
