package store.ckin.api.packaging.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.packaging.dto.request.PackagingUpdateRequestDto;

/**
 * 포장 정책 엔티티입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */

@Getter
@Entity
@Table(name = "Packaging")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Packaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packaging_id")
    private Long packagingId;

    @Column(name = "packaging_type")
    private String packagingType;

    @Column(name = "packaging_price")
    private Integer packagingPrice;

    /**
     * 포장지 정책 생성을 위한 빌더입니다.
     *
     * @param packagingId    포장 정책 ID
     * @param packagingType  포장지 종류
     * @param packagingPrice 포장지 가격
     */
    @Builder
    public Packaging(Long packagingId, String packagingType, Integer packagingPrice) {
        this.packagingId = packagingId;
        this.packagingType = packagingType;
        this.packagingPrice = packagingPrice;
    }

    public void update(PackagingUpdateRequestDto requestDto) {
        this.packagingId = requestDto.getPackagingId();
        this.packagingType = requestDto.getPackagingType();
        this.packagingPrice = requestDto.getPackagingPrice();
    }
}
