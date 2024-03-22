package store.ckin.api.grade.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.grade.domain.request.GradeCreateRequestDto;
import store.ckin.api.grade.domain.request.GradeUpdateRequestDto;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeAlreadyExistsException;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.repository.GradeRepository;

/**
 * GradeService 테스트 코드 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 22.
 */
@ExtendWith(MockitoExtension.class)
class GradeServiceImplTest {
    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @Test
    @DisplayName("등급 생성")
    void testCreateGrade() {
        GradeCreateRequestDto dto = new GradeCreateRequestDto();

        ReflectionTestUtils.setField(dto, "id", 1L);
        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 5);
        ReflectionTestUtils.setField(dto, "condition", 100000);

        when(gradeRepository.existsById(dto.getId()))
                .thenReturn(false);

        gradeService.createGrade(dto);

        verify(gradeRepository).existsById(dto.getId());
        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    @DisplayName("등급 생성 실패")
    void testCreateGradeFailed() {
        GradeCreateRequestDto dto = new GradeCreateRequestDto();

        ReflectionTestUtils.setField(dto, "id", 1L);
        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 5);
        ReflectionTestUtils.setField(dto, "condition", 100000);

        when(gradeRepository.existsById(dto.getId()))
                .thenReturn(true);

        assertThrows(GradeAlreadyExistsException.class,
                () -> gradeService.createGrade(dto));

        verify(gradeRepository).existsById(dto.getId());
        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    @DisplayName("모든 등급 조회")
    void testGetGradeList() {
        Grade normalGrade = Grade.builder()
                .id(1L)
                .name("일반")
                .pointRatio(5)
                .condition(0)
                .build();

        Grade royalGrade = Grade.builder()
                .id(2L)
                .name("로얄")
                .pointRatio(10)
                .condition(100000)
                .build();

        when(gradeRepository.findAll())
                .thenReturn(List.of(normalGrade, royalGrade));

        List<Grade> result = gradeService.getGradeList();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(100000, result.get(1).getCondition());

        verify(gradeRepository).findAll();
    }

    @Test
    @DisplayName("등급 변경")
    void testUpdateGrade() {
        Grade grade = Grade.builder()
                .id(1L)
                .name("일반")
                .pointRatio(5)
                .condition(0)
                .build();

        GradeUpdateRequestDto dto = new GradeUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 10);
        ReflectionTestUtils.setField(dto, "condition", 100000);

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        gradeService.updateGrade(1L, dto);

        assertEquals(10, grade.getPointRatio());

        verify(gradeRepository).findById(1L);
    }

    @Test
    @DisplayName("등급 변경 실패")
    void testUpdateGradeFailed() {
        GradeUpdateRequestDto dto = new GradeUpdateRequestDto();

        ReflectionTestUtils.setField(dto, "name", "일반");
        ReflectionTestUtils.setField(dto, "pointRatio", 10);
        ReflectionTestUtils.setField(dto, "condition", 100000);

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(GradeNotFoundException.class,
                () -> gradeService.updateGrade(1L, dto));

        verify(gradeRepository).findById(1L);
    }

    @Test
    @DisplayName("등급 삭제")
    void testDeleteGrade() {
        when(gradeRepository.existsById(1L))
                .thenReturn(true);

        gradeService.deleteGrade(1L);

        verify(gradeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("등급 삭제 실패")
    void testDeleteGradeFailed() {
        when(gradeRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(GradeNotFoundException.class,
                () -> gradeService.deleteGrade(1L));

        verify(gradeRepository, never()).deleteById(1L);
    }
}