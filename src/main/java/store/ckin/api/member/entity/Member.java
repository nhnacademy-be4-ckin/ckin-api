package store.ckin.api.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import store.ckin.api.grade.entity.Grade;


/**
 * Member 테이블에 대한 Entity 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Builder
@Getter
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "Member")
public class Member {

    /**
     * Member 의 상태를 나타내는 Enum 입니다.
     *
     * @author : jinwoolee
     * @version : 2024. 02. 16.
     */
    public enum State {
        ACTIVE,
        DORMANT,
        WITHDRAWAL
    }

    /**
     * Member 의 역할을 나타내는 Enum 입니다.
     *
     * @author : jinwoolee
     * @version : 2024. 02. 16.
     */

    public enum Role {
        MEMBER,
        ADMIN
    }

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "member_email", unique = true)
    private String email;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_contact", unique = true)
    private String contact;

    @Column(name = "member_birth")
    private LocalDate birth;

    @Column(name = "member_state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "member_latest_login_at")
    private LocalDateTime latestLoginAt;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "member_point")
    private Integer point;

    @Column(name = "member_accumulate_amount")
    @ColumnDefault("0")
    private Integer accumulateAmount;


    public void updatePoint(Integer pointUsage) {
        this.point += pointUsage;
    }
}
