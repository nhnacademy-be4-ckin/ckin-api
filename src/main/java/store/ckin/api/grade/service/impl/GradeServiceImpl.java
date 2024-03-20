package store.ckin.api.grade.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ckin.api.grade.domain.request.GradeRequestDto;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.service.GradeService;

import java.util.List;

/**
 * GradeService 에 대한 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeService gradeService;

    @Override
    public void createGrade(GradeRequestDto gradeRequestDto) {

    }

    @Override
    public List<Grade> getGradeList() {
        return null;
    }

    @Override
    public void updateGrade(GradeRequestDto gradeRequestDto) {

    }

    @Override
    public void deleteGrade(Long gradeId) {

    }
}
