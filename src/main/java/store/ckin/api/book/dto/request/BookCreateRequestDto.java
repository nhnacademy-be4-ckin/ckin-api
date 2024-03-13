package store.ckin.api.book.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * BookCreateRequestDto 클래스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Getter
@NoArgsConstructor
public class BookCreateRequestDto {
    @NotBlank(message = "ISBN을 입력해주세요.")
    @Length(max = 17, message = "ISBN은 최대 17자까지 가능합니다.")
    private String bookIsbn;

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 100, message = "제목은 최대 100자까지 가능합니다.")
    private String bookTitle;

    @NotBlank(message = "책 설명을 입력해주세요.")
    @Length(max = 8000, message = "책 설명은 최대 8000자까지 가능합니다.")
    private String bookDescription;

    @NotBlank(message = "출판사 이름을 입력해주세요.")
    @Length(max = 100, message = "출판사 이름은 최대 100자까지 가능합니다.")
    private String bookPublisher;

    @NotNull(message = "출판일을 입력해주세요.")
    private LocalDate bookPublicationDate;

    @Length(max = 8000, message = "책 목차는 최대 8000자까지 가능합니다.")
    private String bookIndex;

    @NotNull(message = "포장 여부를 선택해주세요.")
    private Boolean bookPackaging;

    @NotBlank(message = "책 상태를 입력해주세요.")
    @Length(max = 20, message = "책 상태는 최대 20자까지 가능합니다.")
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

    @NotEmpty(message = "저자를 최소 한 명 이상 선택해주세요.")
    private Set<Long> authorIds;

    @NotEmpty(message = "카테고리를 최소 한 개 이상 선택해주세요.")
    private Set<Long> categoryIds;

    private Set<Long> tagIds;
}
