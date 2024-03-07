package store.ckin.api.book.relationship.bookcategory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 도서 카테고리 응답 DTO 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 28.
 */

@Getter
@AllArgsConstructor
public class BookCategoryResponseDto {

    private Long bookId;

    private Long categoryId;


}
