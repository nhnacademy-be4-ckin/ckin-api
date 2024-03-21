package store.ckin.api.grade.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.grade.domain.request.GradeCreateRequestDto;
import store.ckin.api.grade.domain.request.GradeUpdateRequestDto;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.grade.exception.GradeAlreadyExistsException;
import store.ckin.api.grade.exception.GradeNotFoundException;
import store.ckin.api.grade.service.GradeService;

/**
 * Grade 에 관한 REST Controller 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/grades")
public class GradeController {
    private final GradeService gradeService;

    /**
     * 등급을 생성하는 메서드.
     *
     * @param gradeCreateRequestDto the grade request dto
     * @return CREATED (Code 201)
     */
    @PostMapping
    public ResponseEntity<Void> createGrade(@Valid @RequestBody GradeCreateRequestDto gradeCreateRequestDto) {
        gradeService.createGrade(gradeCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Grade>> getGradeList() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(gradeService.getGradeList());
    }

    /**
     * 등급을 수정하는 메서드.
     *
     * @return OK (Code 200)
     */
    @PostMapping("/{gradeId}")
    public ResponseEntity<Void> updateGrade(@PathVariable("gradeId") Long gradeId,
                                            @Valid @RequestBody GradeUpdateRequestDto gradeUpdateRequestDto) {
        gradeService.updateGrade(gradeId, gradeUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 등급을 삭제하는 메서드.
     *
     * @return OK (Code 200)
     */
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable("gradeId") Long gradeId) {
        gradeService.deleteGrade(gradeId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler({GradeAlreadyExistsException.class})
    public ResponseEntity<Void> gradeAlreadyExistsExceptionHandler() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler({GradeNotFoundException.class})
    public ResponseEntity<Void> gradeNotFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
