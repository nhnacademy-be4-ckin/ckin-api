package store.ckin.api.cart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.member.entity.Member;

/**
 * description:
 *
 * @author : dduneon
 * @version : 2024. 03. 26
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table
public class Cart {
    @MapsId("userId")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Member member;

    @Id
    @Column
    private Long userId;

    @Column(name = "cart_id")
    private String cartId;
}
