package store.ckin.api.pointpolicy.exception;

/**
 * 포인트 정책 ID가 중복된 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */
public class PointPolicyAlreadyExistsException extends RuntimeException {

    public PointPolicyAlreadyExistsException(Long id, String name) {
        super("PointPolicy already exists [id = " + id + ", name = " + name + "]");
    }
}
