package store.ckin.api.pointpolicy.exception;

/**
 * 포인트 정책이 존재하지 않는 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */
public class PointPolicyNotFoundException extends RuntimeException {

    public PointPolicyNotFoundException(Long id) {
        super(String.format("PointPolicy not found [id = %d]", id));
    }
}
