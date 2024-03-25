package store.ckin.api.book.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * BookMainPageResponseDto.
 *
 * @author 나국로
 * @version 2024. 03. 15.
 */
@Getter
@Builder
@AllArgsConstructor
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

}
