package store.ckin.api.packaging.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포장 정책 응답 DTO.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PackagingResponseDto {

    private Long packagingId;

    private String packagingType;

    private Integer packagingPrice;

    /**
     * 포장 정책 생성을 위한 빌더입니다.
     *
     * @param packagingId    포장 정책 ID
     * @param packagingType  포장지 종류
     * @param packagingPrice 포장지 가격
     */
    @Builder
    public PackagingResponseDto(Long packagingId, String packagingType, Integer packagingPrice) {
        this.packagingId = packagingId;
        this.packagingType = packagingType;
        this.packagingPrice = packagingPrice;
    }
}
