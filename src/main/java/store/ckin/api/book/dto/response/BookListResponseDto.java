package store.ckin.api.book.dto.response;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BookListResponseDto 리스트로 받아올 떄 Dto입니다.
 *
 * @author 나국로
 * @version 2024. 02. 27.
 */
@Getter
@NoArgsConstructor
public class BookListResponseDto {
    private Long bookId;
    private String bookIsbn;
    private String bookTitle;
    private String bookDescription;
    private String bookPublisher;
    private Date bookPublicationDate;
    private String bookIndex;
    private Boolean bookPackaging;
    private Integer bookStock;
    private Integer bookRegularPrice;
    private Integer bookDiscountRate;
    private String bookState;
    private Integer bookSalePrice;
    private String bookReviewRate;
    private List<String> authorNames;

    @Builder
    public BookListResponseDto(Long bookId, String bookIsbn, String bookTitle, String bookDescription,
                               String bookPublisher,
                               Date bookPublicationDate, String bookIndex, Boolean bookPackaging, Integer bookStock,
                               Integer bookRegularPrice, Integer bookDiscountRate, String bookState,
                               Integer bookSalePrice,
                               String bookReviewRate, List<String> authorNames) {
        this.bookId = bookId;
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.bookDescription = bookDescription;
        this.bookPublisher = bookPublisher;
        this.bookPublicationDate = bookPublicationDate;
        this.bookIndex = bookIndex;
        this.bookPackaging = bookPackaging;
        this.bookStock = bookStock;
        this.bookRegularPrice = bookRegularPrice;
        this.bookDiscountRate = bookDiscountRate;
        this.bookState = bookState;
        this.bookSalePrice = bookSalePrice;
        this.bookReviewRate = bookReviewRate;
        this.authorNames = authorNames;
    }
}
