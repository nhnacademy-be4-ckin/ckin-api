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
import store.ckin.api.wishlist.entity.WishList;
import store.ckin.api.wishlist.exception.WishListAlreadyExistsException;
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
    public void createWishList(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        WishList.Pk pk = new WishList.Pk(bookId, memberId);

        if (wishListRepository.existsByPk(pk)) {
            throw new WishListAlreadyExistsException(pk.getMemberId(), pk.getBookId());
        }

        WishList wishList = WishList.builder()
                .member(member)
                .book(book)
                .pk(pk)
                .build();

        wishListRepository.save(wishList);
    }

    @Transactional
    @Override
    public void deleteWishList(Long memberId, Long bookId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        wishListRepository.deleteByPk(new WishList.Pk(bookId, memberId));
    }
}
