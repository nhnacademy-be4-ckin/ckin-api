package store.ckin.api.grade.service;

import java.util.List;
import store.ckin.api.grade.domain.request.GradeRequestDto;
import store.ckin.api.grade.entity.Grade;

/**
 * Grade 의 관한 로직을 처리하는 서비스 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 20.
 */
public interface GradeService {
    void createGrade(GradeRequestDto gradeRequestDto);

    List<Grade> getGradeList();

    void updateGrade(GradeRequestDto gradeRequestDto);

    void deleteGrade(Long gradeId);
}
