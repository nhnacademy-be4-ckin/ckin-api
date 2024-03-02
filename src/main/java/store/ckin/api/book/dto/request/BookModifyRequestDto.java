package store.ckin.api.book.dto.request;

import java.util.Date;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * BookModifyRequestDto.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Getter
@NoArgsConstructor
public class BookModifyRequestDto {
    @Length(max = 17, message = "ISBN을 입력해주세요.")
    private String bookIsbn;

    @Length(max = 100, message = "제목을 입력해주세요.")
    private String bookTitle;

    @Length(max = 8000, message = "책 설명을 입력해주세요.")
    private String bookDescription;

    @Length(max = 100, message = "출판사 이름을 입력해주세요.")
    private String bookPublisher;

    private Date bookPublicationDate;

    @Length(max = 8000, message = "책 목차를 입력해주세요.")
    private String bookIndex;

    private Boolean bookPackaging;

    @Length(max = 20, message = "책 상태를 입력해주세요.")
    private String bookState;

    @NotNull(message = "재고를 입력해주세요.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer bookStock;

    @NotNull(message = "정가를 입력해주세요.")
    @Min(value = 0, message = "정가는 0 이상이어야 합니다.")
    private Integer bookRegularPrice;

    @NotNull(message = "할인율을 입력해주세요.")
    @Min(value = 0, message = "할인율은 0% 이상이어야 합니다.")
    @Max(value = 100, message = "할인율은 100% 이하여야 합니다.")
    private Integer bookDiscountRate;

    @NotEmpty
    private Set<Long> authorIds;
    @NotEmpty
    private Set<Long> categoryIds;
    private Set<Long> tagIds;
}
