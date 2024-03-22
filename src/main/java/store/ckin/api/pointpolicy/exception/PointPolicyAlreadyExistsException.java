package store.ckin.api.pointpolicy.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 포인트 정책 ID가 중복된 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */
public class PointPolicyAlreadyExistsException extends GeneralAlreadyExistsException {

    public PointPolicyAlreadyExistsException(Long id, String name) {
        super(String.format("해당 포인트 정책은 이미 존재합니다. [id = %d, name = %s]", id, name));
    }
}
