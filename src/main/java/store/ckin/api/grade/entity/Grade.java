package store.ckin.api.grade.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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