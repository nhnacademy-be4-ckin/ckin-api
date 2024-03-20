package store.ckin.api.pointhistory.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.member.entity.Member;
import store.ckin.api.sale.entity.Sale;

/**
 * 포인트 내역 엔티티입니다.
 *
 * @author 정승조
 * @version 2024. 03. 15.
 */

@Entity
@Table(name = "PointHistory")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointhistory_id")
    private Long pointHistoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "pointhistory_reason")
    private String pointHistoryReason;

    @Column(name = "pointhistory_point")
    private Integer pointHistoryPoint;

    @Column(name = "pointhistory_time")
    private LocalDate pointHistoryTime;

}
