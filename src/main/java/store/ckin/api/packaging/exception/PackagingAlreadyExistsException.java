package store.ckin.api.packaging.exception;

/**
 * 포장 정책 - 포장지 종류가 중복되는 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 02. 21.
 */
public class PackagingAlreadyExistsException extends RuntimeException {
    public PackagingAlreadyExistsException(String packagingType) {
        super("Packaging already exists [packaging type = " + packagingType + "]");
    }
}
