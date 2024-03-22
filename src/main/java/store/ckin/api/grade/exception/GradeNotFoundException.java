package store.ckin.api.grade.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * Grade 를 찾지 못할 때 호출되는 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 26.
 */
public class GradeNotFoundException extends GeneralNotFoundException {

    public GradeNotFoundException(Long id) {
        super(String.format("등급을 찾을 수 없습니다. [id = %s]", id));
    }
}
