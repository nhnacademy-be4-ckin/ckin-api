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

    @Column("address_base")
    private String base;

    @Column("address_detail")
    private String detail;

    @Column("address_alias")
    private String alias;

    @Column("address_default")
    @ColumnDefault("false")
    private boolean isDefault;
}
