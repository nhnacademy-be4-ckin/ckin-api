package store.ckin.api.member.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * 정보에 일치하는 Member 정보가 없을 때 호출 되는 Exception 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
public class MemberNotFoundException extends GeneralNotFoundException {
    public MemberNotFoundException(Long id) {
        super(String.format("회원을 찾을 수 없습니다. [id = %d]", id));
    }

    public MemberNotFoundException(String email) {
        super(String.format("회원을 찾을 수 없습니다. [id = %s]", email));
    }

}
