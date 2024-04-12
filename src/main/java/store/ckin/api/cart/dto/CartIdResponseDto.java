package store.ckin.api.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카트 id를 포함하는 응답을 담는 Dto 클래스
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartIdResponseDto {
    private String cartId;
}
