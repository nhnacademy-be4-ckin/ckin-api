package store.ckin.api.book.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 도서 추출 정보 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 28.
 */

@Getter
@Builder
public class BookExtractionResponseDto {

    private Long bookId;

    private String bookImageUrl;

    private String bookTitle;

    private Boolean bookPackaging;

    private Integer bookSalePrice;

    private Integer bookStock;

    private final List<Long> categoryIds = new ArrayList<>();

    public BookExtractionResponseDto(Long bookId, String bookImageUrl, String bookTitle, Boolean bookPackaging,
                                     Integer bookSalePrice, Integer bookStock) {
        this.bookId = bookId;
        this.bookImageUrl = bookImageUrl;
        this.bookTitle = bookTitle;
        this.bookPackaging = bookPackaging;
        this.bookSalePrice = bookSalePrice;
        this.bookStock = bookStock;
    }
}
