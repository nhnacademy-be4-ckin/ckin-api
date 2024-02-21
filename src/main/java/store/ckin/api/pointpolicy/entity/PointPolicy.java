package store.ckin.api.pointpolicy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포인트 정책 엔티티입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */

@Getter
@Entity
@Table(name = "PointPolicy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointPolicy {

    @Id
    @Column(name = "pointpolicy_id")
    private Long pointPolicyId;

    @Column(name = "pointpolicy_name", unique = true)
    private String pointPolicyName;

    @Column(name = "pointpolicy_reserve")
    private Integer pointPolicyReserve;

    /**
     * 포인트 정책 생성을 위한 빌더입니다.
     *
     * @param pointPolicyId      포인트 정책 아이디
     * @param pointPolicyName    포인트 정책 이름
     * @param pointPolicyReserve 포인트 적립 금액
     */
    @Builder
    public PointPolicy(Long pointPolicyId, String pointPolicyName, Integer pointPolicyReserve) {
        this.pointPolicyId = pointPolicyId;
        this.pointPolicyName = pointPolicyName;
        this.pointPolicyReserve = pointPolicyReserve;
    }

    public void update(Long pointPolicyId, String pointPolicyName, Integer pointPolicyReserve) {
        this.pointPolicyId = pointPolicyId;
        this.pointPolicyName = pointPolicyName;
        this.pointPolicyReserve = pointPolicyReserve;
    }
}
