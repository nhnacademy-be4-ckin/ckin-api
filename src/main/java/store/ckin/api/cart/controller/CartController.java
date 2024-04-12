package store.ckin.api.cart.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.cart.dto.CartCreateRequestDto;
import store.ckin.api.cart.dto.CartIdResponseDto;
import store.ckin.api.cart.service.CartService;

/**
 * Cart 서비스를 제공하는 REST Controller 입니다
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    /**
     * 유저의 아이디를 통하여 카트 아이디를 가져오는 메서드
     * @param memberId 사용자의 user_id
     * @return 사용자의 카트 ID(UUID)
     */
    @GetMapping
    public ResponseEntity<CartIdResponseDto> getUserCartId(@RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(cartService.readCartId(memberId));
    }

    /**
     * 유저의 카트 데이터를 생성하는 메서드
     * @param cartCreateRequestDto userId, cartId
     * @return 생성되었다면 CREATED
     */
    @PostMapping
    public ResponseEntity<Void> createUserCart(@Valid @RequestBody CartCreateRequestDto cartCreateRequestDto) {
        cartService.createCart(cartCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
