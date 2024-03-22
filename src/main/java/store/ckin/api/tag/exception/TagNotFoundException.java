package store.ckin.api.tag.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 존재하지 않는 태그 아이디일 때 발생하는 TagNotFoundException.
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
public class TagNotFoundException extends GeneralNotFoundException {
    public TagNotFoundException(Long tagId) {
        super(String.format("태그(id: %d)를 찾을 수 없습니다", tagId));
    }
}
