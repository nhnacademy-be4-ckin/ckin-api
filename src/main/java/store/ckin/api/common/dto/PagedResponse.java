package store.ckin.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import store.ckin.api.common.domain.PageInfo;

/**
 * Paging 된 객체와 Page 정보를 담는 클래스
 *
 * @author 김준현
 * @version 2024. 02. 20
 */

@AllArgsConstructor
@Getter
public class PagedResponse<T> {
    private T data;
    private PageInfo pageInfo;
}
