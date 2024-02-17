package store.ckin.api.tag.exception;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException(Long tagId) {
        super(String.format("태그(id: %d)를 찾을 수 없습니다", tagId));
    }
}
