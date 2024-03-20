package store.ckin.api.grade.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.grade.domain.request.GradeRequestDto;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeAlreadyExistsException;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;
import store.ckin.api.grade.service.GradeService;

/**
 * GradeService 에 대한 구현체 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;

    @Transactional
    @Override
    public void createGrade(GradeRequestDto gradeRequestDto) {
        if (gradeRepository.existsById(gradeRequestDto.getId())) {
            throw new GradeAlreadyExistsException();
        }

        Grade grade = Grade.builder()
                .id(gradeRequestDto.getId())
                .name(gradeRequestDto.getName())
                .pointRatio(gradeRequestDto.getPointRatio())
                .condition(gradeRequestDto.getCondition())
                .build();

        gradeRepository.save(grade);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Grade> getGradeList() {
        return gradeRepository.findAll();
    }

    @Transactional
    @Override
    public void updateGrade(GradeRequestDto gradeRequestDto) {
        Grade grade = gradeRepository.findById(gradeRequestDto.getId())
                .orElseThrow(GradeNotFoundException::new);

        grade.update(gradeRequestDto);
    }

    @Transactional
    @Override
    public void deleteGrade(Long gradeId) {
        if (gradeRepository.existsById(gradeId)) {
            throw new GradeAlreadyExistsException();
        }

        gradeRepository.deleteById(gradeId);
    }
}
