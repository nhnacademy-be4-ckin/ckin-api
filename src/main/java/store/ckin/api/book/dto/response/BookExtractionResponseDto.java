package store.ckin.api.book.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 도서 추출 정보 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 28.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookExtractionResponseDto {

    private Long bookId;

    private String bookImageUrl;

    private String bookTitle;

    private Boolean bookPackaging;

    private Integer bookSalePrice;

    private Integer bookStock;

    private List<Long> categoryIds;

    public BookExtractionResponseDto(Long bookId, String bookImageUrl, String bookTitle, Boolean bookPackaging,
                                     Integer bookSalePrice, Integer bookStock) {
        this.bookId = bookId;
        this.bookImageUrl = bookImageUrl;
        this.bookTitle = bookTitle;
        this.bookPackaging = bookPackaging;
        this.bookSalePrice = bookSalePrice;
        this.bookStock = bookStock;
        this.categoryIds = new ArrayList<>();
    }

    @Builder
    public BookExtractionResponseDto(Long bookId, String bookImageUrl, String bookTitle, Boolean bookPackaging,
                                     Integer bookSalePrice, Integer bookStock, List<Long> categoryIds) {
        this.bookId = bookId;
        this.bookImageUrl = bookImageUrl;
        this.bookTitle = bookTitle;
        this.bookPackaging = bookPackaging;
        this.bookSalePrice = bookSalePrice;
        this.bookStock = bookStock;
        this.categoryIds = categoryIds;
    }
}
