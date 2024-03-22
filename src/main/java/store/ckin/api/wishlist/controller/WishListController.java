package store.ckin.api.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.member.exception.MemberAlreadyExistsException;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.wishlist.exception.WishListAlreadyExistsException;
import store.ckin.api.wishlist.service.WishListService;

/**
 * Wish List 에 관한 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WishListController {
    private final WishListService wishListService;

    /**
     * 위시리스트에 추가하는 메서드 입니다.
     */
    @PostMapping("/members/{memberId}/wish-list/{bookId}")
    public ResponseEntity<Void> addWishList(@PathVariable("memberId") Long memberId,
                                            @PathVariable("bookId") Long bookId) {
        wishListService.createWishList(memberId, bookId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 위시리스트에서 제거하는 메서드 입니다.
     */
    @DeleteMapping("/members/{memberId}/wish-list/{bookId}")
    public ResponseEntity<Void> deleteWishList(@PathVariable("memberId") Long memberId,
                                            @PathVariable("bookId") Long bookId) {
        wishListService.deleteWishList(memberId, bookId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler({WishListAlreadyExistsException.class})
    public ResponseEntity<Void> memberAlreadyExistsExceptionHandler() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler({MemberNotFoundException.class, BookNotFoundException.class})
    public ResponseEntity<Void> memberNotFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
