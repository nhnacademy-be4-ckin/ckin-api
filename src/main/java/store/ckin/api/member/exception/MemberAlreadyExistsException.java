package store.ckin.api.member.exception;

import store.ckin.api.advice.exception.GeneralAlreadyExistsException;

/**
 * 이미 존재하는 계정일 때 발생하는 Exception 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
public class MemberAlreadyExistsException extends GeneralAlreadyExistsException {
    public MemberAlreadyExistsException() {
        super("해당 이메일은 이미 사용중입니다.");
    }
}
