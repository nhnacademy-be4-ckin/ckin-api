package store.ckin.api.member.exception;

import store.ckin.api.advice.exception.GeneralBadRequestException;

/**
 * 보유 포인트보다 더 많은 포인트를 사용할 경우 호출되는 Exception 입니다.
 *
 * @author 정승조
 * @version 2024. 03. 27.
 */
public class MemberPointNotEnoughException extends GeneralBadRequestException {

    public MemberPointNotEnoughException() {
        super("보유 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
    }
}
