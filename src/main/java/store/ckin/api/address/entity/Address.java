package store.ckin.api.address.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import store.ckin.api.address.domain.request.AddressUpdateRequestDto;
import store.ckin.api.member.entity.Member;

/**
 * Address Table 의 Entity Class 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 17.
 */
@Builder
@Getter
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "Address")
public class Address {
    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "address_base")
    private String base;

    @Column(name = "address_detail")
    private String detail;

    @Column(name = "address_alias")
    private String alias;

    @Column(name = "address_default")
    @ColumnDefault("false")
    private Boolean isDefault;

    /**
     * 주소를 수정하는 메서드 입니다.
     *
     * @param addressUpdateRequestDto 기본주소, 상세주소, 별칭
     */
    public void update(AddressUpdateRequestDto addressUpdateRequestDto) {
        this.base = addressUpdateRequestDto.getBase();
        this.detail = addressUpdateRequestDto.getDetail();
        this.alias = addressUpdateRequestDto.getAlias();
    }
}
