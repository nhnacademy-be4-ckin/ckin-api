package store.ckin.api.book.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BookMainPageResponseDto.
 *
 * @author 나국로
 * @version 2024. 03. 15.
 */
@Getter
@NoArgsConstructor
public class BookMainPageResponseDto {
    private Long bookId;
    private String bookTitle;
    private Integer bookRegularPrice;
    private Integer bookDiscountRate;
    private Integer bookSalePrice;
    private String thumbnail;
    private List<String> productCategories;
    private List<String> productAuthorNames;
    private List<String> productTags;

    @Builder
    public BookMainPageResponseDto(Long bookId, String bookTitle, Integer bookRegularPrice, Integer bookDiscountRate,
                                   Integer bookSalePrice, String thumbnail, List<String> productCategories,
                                   List<String> productAuthorNames, List<String> productTags) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookRegularPrice = bookRegularPrice;
        this.bookDiscountRate = bookDiscountRate;
        this.bookSalePrice = bookSalePrice;
        this.thumbnail = thumbnail;
        this.productCategories = productCategories;
        this.productAuthorNames = productAuthorNames;
        this.productTags = productTags;
    }
}
