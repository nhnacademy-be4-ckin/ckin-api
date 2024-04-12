package store.ckin.api.cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카트 생성 요청을 위한 Dto 클래스
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
@Getter
@NoArgsConstructor
public class CartCreateRequestDto {
    @NotNull
    private Long memberId;
    @NotBlank
    private String cartId;
}
