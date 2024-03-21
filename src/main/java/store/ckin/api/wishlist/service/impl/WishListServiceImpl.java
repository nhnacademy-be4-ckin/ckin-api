package store.ckin.api.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.wishlist.domain.request.WishListRequestDto;
import store.ckin.api.wishlist.entity.WishList;
import store.ckin.api.wishlist.repository.WishListRepository;
import store.ckin.api.wishlist.service.WishListService;

/**
 * WishListService 의 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final MemberRepository memberRepository;

    private final BookRepository bookRepository;

    private final WishListRepository wishListRepository;

    @Transactional
    @Override
    public void createWishList(WishListRequestDto wishListRequestDto) {
        Member member = memberRepository.findById(wishListRequestDto.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Book book = bookRepository.findByBookId(wishListRequestDto.getBookId())
                .orElseThrow(BookNotFoundException::new);

        WishList wishList = WishList.builder()
                .member(member)
                .book(book)
                .pk(new WishList.PK(
                        wishListRequestDto.getBookId(),
                        wishListRequestDto.getMemberId()))
                .build();

        wishListRepository.save(wishList);
    }

    @Transactional
    @Override
    public void deleteWishList(WishListRequestDto wishListRequestDto) {
        if (!memberRepository.existsById(wishListRequestDto.getMemberId())) {
            throw new MemberNotFoundException();
        }

        if (!bookRepository.existsById(wishListRequestDto.getBookId())) {
            throw new BookNotFoundException();
        }

        wishListRepository.deleteByPk(
                new WishList.PK(
                        wishListRequestDto.getBookId(),
                        wishListRequestDto.getMemberId()));
    }
}
