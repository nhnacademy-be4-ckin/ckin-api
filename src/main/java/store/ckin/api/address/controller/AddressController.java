package store.ckin.api.address.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
