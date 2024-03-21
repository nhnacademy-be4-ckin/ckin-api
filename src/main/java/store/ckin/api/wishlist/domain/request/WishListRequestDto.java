package store.ckin.api.wishlist.domain.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * WishList 요청에 필요한 DTO 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@Getter
@NoArgsConstructor
public class WishListRequestDto {
    @NotNull
    private Long memberId;

    @NotNull
    private Long bookId;
}
