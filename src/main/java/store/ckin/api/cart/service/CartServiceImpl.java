package store.ckin.api.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.cart.dto.CartCreateRequestDto;
import store.ckin.api.cart.dto.CartIdResponseDto;
import store.ckin.api.cart.entity.Cart;
import store.ckin.api.cart.exception.CartAlreadyExistException;
import store.ckin.api.cart.exception.CartNotFoundException;
import store.ckin.api.cart.repository.CartRepository;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;

/**
 * Cart CRUD 를 제공하는 서비스 클래스
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createCart(CartCreateRequestDto cartCreateRequestDto) {
        Long memberId = cartCreateRequestDto.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        String cartId = cartCreateRequestDto.getCartId();
        if(cartRepository.existsById(memberId)) {
            throw new CartAlreadyExistException(memberId);
        }
        Cart cart = Cart.builder()
                .member(member)
                .memberId(memberId)
                .cartId(cartId)
                .build();

        cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartIdResponseDto readCartId(Long memberId) {
        if (!cartRepository.existsById(memberId)) {
            throw new CartNotFoundException(memberId);
        }

        return cartRepository.getCartIdByMemberId(memberId);
    }
}
