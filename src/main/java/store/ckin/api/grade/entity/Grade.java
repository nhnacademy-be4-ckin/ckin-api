package store.ckin.api.grade.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Grade 테이블에 대한 Entity 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "Grade")
public class Grade {
    @Id
    @Column(name = "grade_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gradeId;

    @Column(name = "grade_name", unique = true)
    private String name;

    @Column(name = "grade_point_ratio")
    private Integer pointRatio;
}