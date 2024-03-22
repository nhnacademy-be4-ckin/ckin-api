package store.ckin.api.pointpolicy.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 포인트 정책이 존재하지 않는 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */
public class PointPolicyNotFoundException extends GeneralNotFoundException {

    public PointPolicyNotFoundException(Long id) {
        super(String.format("포인트 정책을 찾을 수 없습니다. [id = %d]", id));
    }
}
