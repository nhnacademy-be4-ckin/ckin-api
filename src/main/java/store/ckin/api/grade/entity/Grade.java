package store.ckin.api.grade.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.ckin.api.grade.domain.request.GradeUpdateRequestDto;

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
    private Long id;

    @Column(name = "grade_name", unique = true)
    private String name;

    @Column(name = "grade_point_ratio")
    private Integer pointRatio;

    @Column(name = "grade_condition")
    private Integer condition;

    /**
     * 등급 수정하는 메서드 입니다.
     */
    public void update(GradeUpdateRequestDto gradeUpdateRequestDto) {
        this.name = gradeUpdateRequestDto.getName();
        this.pointRatio = gradeUpdateRequestDto.getPointRatio();
        this.condition = gradeUpdateRequestDto.getCondition();
    }
}