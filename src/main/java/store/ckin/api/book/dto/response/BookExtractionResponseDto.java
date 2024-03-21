package store.ckin.api.book.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class BookExtractionResponseDto {

    private Long bookId;

    private String bookImageUrl;

    private String bookTitle;

    private Boolean bookPackaging;

    private Integer bookSalePrice;

    private Integer bookStock;

    private List<Long> categoryIds;
}
