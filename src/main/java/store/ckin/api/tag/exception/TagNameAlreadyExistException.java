package store.ckin.api.tag.exception;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
public class TagNameAlreadyExistException extends RuntimeException {
    public TagNameAlreadyExistException(String tagName) {
        super(String.format("태그(name: %s)는 이미 존재하는 태그 이름입니다", tagName));
    }
}
