package store.ckin.api.member.exception;

import store.ckin.api.advice.exception.GeneralBadRequestException;

/**
 * 계정 상태를 바꿀 수 없을 때 호출 되는 Exception 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 21.
 */
public class MemberCannotChangeStateException extends GeneralBadRequestException {

    public MemberCannotChangeStateException() {
        super("계정의 상태를 바꿀 수 없습니다.");
    }
}
