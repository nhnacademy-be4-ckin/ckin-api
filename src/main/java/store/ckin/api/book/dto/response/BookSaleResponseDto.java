package store.ckin.api.book.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서 주문 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 28.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookSaleResponseDto {

    private Long bookId;

    private String bookTitle;

    private Boolean bookPackaging;

    private Integer bookSalePrice;

    @Builder
    public BookSaleResponseDto(Long bookId, String bookTitle, Boolean bookPackaging, Integer bookSalePrice) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookPackaging = bookPackaging;
        this.bookSalePrice = bookSalePrice;
    }
}
