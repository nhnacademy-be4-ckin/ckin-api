package store.ckin.api.grade.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.ckin.api.grade.domain.request.GradeCreateRequestDto;
import store.ckin.api.grade.domain.request.GradeUpdateRequestDto;
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
    public void createGrade(GradeCreateRequestDto gradeCreateRequestDto) {
        if (gradeRepository.existsById(gradeCreateRequestDto.getId())) {
            throw new GradeAlreadyExistsException();
        }

        Grade grade = Grade.builder()
                .id(gradeCreateRequestDto.getId())
                .name(gradeCreateRequestDto.getName())
                .pointRatio(gradeCreateRequestDto.getPointRatio())
                .condition(gradeCreateRequestDto.getCondition())
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
    public void updateGrade(Long gradeId, GradeUpdateRequestDto gradeUpdateRequestDto) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(GradeNotFoundException::new);

        grade.update(gradeUpdateRequestDto);
    }

    @Transactional
    @Override
    public void deleteGrade(Long gradeId) {
        if (!gradeRepository.existsById(gradeId)) {
            throw new GradeNotFoundException();
        }

        gradeRepository.deleteById(gradeId);
    }
}
