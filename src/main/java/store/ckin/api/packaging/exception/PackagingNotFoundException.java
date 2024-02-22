package store.ckin.api.packaging.exception;

/**
 * 포장 정책이 존재하지 않는 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 12.
 */
public class PackagingNotFoundException extends RuntimeException {

    public PackagingNotFoundException(Long id) {
        super(String.format("Packaging not found [packaging id = %d]", id));
    }
}
