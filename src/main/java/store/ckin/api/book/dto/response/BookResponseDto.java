package store.ckin.api.book.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * BookResponseDto 단일 조회 dto클래스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Getter
@NoArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String bookIsbn;
    private String bookTitle;
    private String bookDescription;
    private String bookPublisher;
    private LocalDate bookPublicationDate;
    private String bookIndex;
    private Boolean bookPackaging;
    private Integer bookStock;
    private Integer bookRegularPrice;
    private Integer bookDiscountRate;
    private String bookState;
    private Integer bookSalePrice;
    private String bookReviewRate;
    private String thumbnail;
    private List<String> authorNames;
    private List<String> categoryNames;
    private List<String> tagNames;

    @Builder
    public BookResponseDto(Long bookId, String bookIsbn, String bookTitle, String bookDescription, String bookPublisher,
                           LocalDate bookPublicationDate, String bookIndex, Boolean bookPackaging, Integer bookStock,
                           Integer bookRegularPrice, Integer bookDiscountRate, String bookState, Integer bookSalePrice,
                           String bookReviewRate, String thumbnail, List<String> authorNames,
                           List<String> categoryNames,
                           List<String> tagNames) {
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
        this.thumbnail = thumbnail;
        this.authorNames = authorNames;
        this.categoryNames = categoryNames;
        this.tagNames = tagNames;
    }
}
