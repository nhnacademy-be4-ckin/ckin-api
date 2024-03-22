package store.ckin.api.grade.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 이미 존재하는 ID의 등급이 있을 때 호출되는 메서드 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
public class GradeAlreadyExistsException extends GeneralAlreadyExistsException {

    public GradeAlreadyExistsException(Long id) {
        super(String.format("등급의 ID가 존재합니다. [id = %d]", id));
    }
}
