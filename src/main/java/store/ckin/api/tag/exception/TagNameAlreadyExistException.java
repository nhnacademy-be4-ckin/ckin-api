package store.ckin.api.tag.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 이미 존재하는 태그 이름일 때 발생하는 TagNameAlreadyExistException.
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
public class TagNameAlreadyExistException extends GeneralAlreadyExistsException {
    public TagNameAlreadyExistException(String tagName) {
        super(String.format("이미 존재하는 태그 이름입니다. [tag Name = %s]", tagName));
    }
}
